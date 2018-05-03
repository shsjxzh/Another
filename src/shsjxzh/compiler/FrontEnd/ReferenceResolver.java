package shsjxzh.compiler.FrontEnd;

import shsjxzh.compiler.AST.ASTNode;
import shsjxzh.compiler.AST.ASTVisitor;
import shsjxzh.compiler.AST.Decl.ClassDeclNode;
import shsjxzh.compiler.AST.Decl.DeclNode;
import shsjxzh.compiler.AST.Decl.FuncDeclNode;
import shsjxzh.compiler.AST.Decl.VarDeclNode;
import shsjxzh.compiler.AST.Expr.*;
import shsjxzh.compiler.AST.ProgramNode;
import shsjxzh.compiler.AST.Stmt.*;
import shsjxzh.compiler.AST.tool.Position;
import shsjxzh.compiler.ErrorHandle.ErrorHandler;
import shsjxzh.compiler.Scope.GlobalScope;
import shsjxzh.compiler.Scope.LocalScope;
import shsjxzh.compiler.Scope.Scope;
import shsjxzh.compiler.Type.Type;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReferenceResolver implements ASTVisitor {
    private GlobalScope globalScope;
    private Scope currentScope;

    //the latest
    //preprocess class
    private Map<String, Scope> tmpPreprocessScope;
    private Map<String, ClassDeclNode> typeDefinitions;
    private Map<String, Scope> tmpClassMethodScope;

    public ReferenceResolver(){
        //aim:
        //check undefined variables and functions
        //right type using(operator, return type, function calls)
        globalScope = new GlobalScope();
        currentScope = globalScope;
        tmpPreprocessScope = new HashMap<>();
        typeDefinitions = new HashMap<>();
        tmpClassMethodScope = new HashMap<>();
    }
    public GlobalScope getGlobalScope() {
        return globalScope;
    }

    //check without setting the expr type
    private void check(ASTNode node) {
        if (node != null) {
            node.accept(this);
        }
    }

    //check with the expr setting the type
    private void checkAndInitType(ExprNode node) {
        if (node != null){
            node.accept(this);
            //ensure that the leaves has been set, so we can initialize the type
            node.initExprType();
        }
        //null means no type, or null type
    }

    //check type reference
    private void checkTypeDefinition(Type type, Position pos) {
        if (type != null) {
            //must be class!!
            ClassDeclNode typeDefinition = typeDefinitions.get(type.getTypeName());
            if (typeDefinition == null) {
                throw new ErrorHandler("Undefined type \"" + type.getTypeName() + "\"", pos);
            }
            type.setTypeDefinition(typeDefinition);
        }
    }

    private void checkFuncParams(List<ExprNode> checkParams, FuncDeclNode func, Position pos){
        if (checkParams.size() != func.getFuncParams().size()){
            throw new ErrorHandler("The size of function params does not match", pos);
        }
        int i = 0;
        for (ExprNode exprNode : checkParams) {
            checkAndInitType(exprNode);
            Type rightType = exprNode.getExprType();
            Type leftType = func.getFuncParams().get(i).getVarType();
            if ( (leftType.equals(rightType))
                    || (leftType.isArray() && rightType == null)
                    || (!leftType.isBuildInType() && rightType == null) ){}
                    else{
                throw new ErrorHandler("Unmatched function params type", exprNode.getPos());
            }
            ++i;
        }
    }

    private void preProcessFunc(FuncDeclNode node) {
        if (node != null) {
            currentScope = new LocalScope("function", node.getName(), currentScope);
            if (!((LocalScope) currentScope).parent.getKind().equals("class")) tmpPreprocessScope.put(node.getName(), currentScope);
            else tmpClassMethodScope.put(node.getName(), currentScope);
            checkTypeDefinition(node.getFuncReturnType(), node.getPos());
            for (VarDeclNode varDeclNode : node.getFuncParams()) {
                check(varDeclNode);
            }

            currentScope = currentScope.getParentScope();
        }
    }
    private void preProssClass(ClassDeclNode node){
        currentScope = new LocalScope("class", node.getName(),currentScope);
        tmpPreprocessScope.put(node.getName(), currentScope);

        //define, not been checked yet
        for (VarDeclNode varDeclNode : node.getClassMember()) {
            check(varDeclNode);
        }
        for (FuncDeclNode funcDeclNode : node.getClassMethod()) {
            currentScope.define(funcDeclNode);
            preProcessFunc(funcDeclNode);
        }
        preProcessFunc(node.getConstructMethod());

        currentScope = currentScope.getParentScope();
    }

    @Override
    public void visit(ProgramNode node) {
        //preprocess for forward reference
        //first preprocess: define all functions and classes(with buildin functions and classes)
        //second preprocess: build method scope and add method params for call check 
        List<DeclNode> decls = node.getDeclnodes();
        for (DeclNode decl : decls) {
            if (decl instanceof FuncDeclNode){
                //define with duplicate check
                currentScope.define(decl);
            }
            if (decl instanceof ClassDeclNode) {
                currentScope.define(decl);
                typeDefinitions.put(decl.getName(), (ClassDeclNode) decl);
            }
        }

        for (DeclNode decl : decls) {
            if (decl instanceof FuncDeclNode){
                preProcessFunc((FuncDeclNode) decl);
            }
            if (decl instanceof ClassDeclNode){
                //test
                //System.out.println(decl.getName());
                preProssClass((ClassDeclNode) decl);
            }

            DeclNode mainDecl = globalScope.entities.get("main");
            if (mainDecl == null) throw new ErrorHandler("missing main function", node.getPos());
            else {
                if (mainDecl instanceof FuncDeclNode){
                    if (((FuncDeclNode) mainDecl).getFuncReturnType() != null
                            && ((FuncDeclNode) mainDecl).getFuncReturnType().isInt()) {}
                            else throw new ErrorHandler("error main function", node.getPos());
                }
            }
        }

        //check all
        for (DeclNode decl : decls) {
            check(decl);
        }
        //when you exit check, it means no errors occur there.

        //return missing check
        /*no return test!
        for (DeclNode decl : decls){
            if (decl instanceof FuncDeclNode){
                if (!((FuncDeclNode) decl).hasReturn()){
                    throw new ErrorHandler("missing return!" , decl.getPos());
                }
            }
        }*/
    }

    @Override
    public void visit(FuncDeclNode node) {
        //function & class method
        //System.out.println(node.getName());
        if (currentScope.getKind().equals("class")){
            currentScope = tmpClassMethodScope.get(node.getName());
        }
        else {
            currentScope = tmpPreprocessScope.get(node.getName());
        }
        check(node.getFuncBlock());

        currentScope = currentScope.getParentScope();
        //block will turn to the parent scope
    }

    @Override
    public void visit(ClassDeclNode node) {
        currentScope = tmpPreprocessScope.get(node.getName());
        check(node.getConstructMethod());

        for (FuncDeclNode funcDeclNode : node.getClassMethod()) {
            check(funcDeclNode);
        }
        currentScope = currentScope.getParentScope();
    }

    @Override
    public void visit(VarDeclNode node) {
        checkTypeDefinition(node.getVarType(), node.getPos());
        if (node.getExpr() != null) {
            checkAndInitType(node.getExpr());
            Type leftType = node.getVarType();
            Type rightType = node.getExpr().exprType;
            if( (leftType.isArray() && rightType == null)
                    || (!leftType.isBuildInType() && rightType == null)
                    || leftType.equals(rightType)) {}
            else{
                throw new ErrorHandler("Unmatched type in value initialization",node.getPos());
            }
        }
        currentScope.define(node);
    }

    @Override
    public void visit(VarDeclStmtNode node) {
        check(node.getVarDecl());
    }

    @Override
    public void visit(BlockNode node) {
        //think about different cases
        currentScope = new LocalScope("block","", currentScope);
        for (StmtNode stmtNode : node.getStmt()) {
            check(stmtNode);
        }
        currentScope = currentScope.getParentScope();
    }

    @Override
    public void visit(BreakStmtNode node) {
        if (!currentScope.resolveBreakContinue()){
            throw new ErrorHandler("Error using of break or continue", node.getPos());
        }
    }

    @Override
    public void visit(ContinueStmtNode node) {
        if (!currentScope.resolveBreakContinue()){
            throw new ErrorHandler("Error using of break or continue", node.getPos());
        }
    }

    @Override
    public void visit(ExprStmtNode node) {
        checkAndInitType(node.expr);
    }

    @Override
    public void visit(ForStmtNode node) {
        currentScope = new LocalScope("for","", currentScope);
        checkAndInitType(node.getBegin_expr());
        check(node.getBegin_varDecl());
        if (node.getBegin_varDecl() != null){
            currentScope.define(node.getBegin_varDecl());
        }
        checkAndInitType(node.getCond());
        //in for's condition, it can be anything?
        checkAndInitType(node.getIter());
        check(node.getBody());
        currentScope = currentScope.getParentScope();
    }

    @Override
    public void visit(IfStmtNode node) {
        checkAndInitType(node.getCond());
        if(node.getCond().exprType == null || !node.getCond().getExprType().isBool() ){
            throw new ErrorHandler("error expression in \"if\"'s condition", node.getPos());
        }

        check(node.getThen());
        check(node.getOtherwise());
    }

    @Override
    public void visit(ReturnStmtNode node) {
        checkAndInitType(node.getReExpr());
        currentScope.resolveReturn(node);
    }

    @Override
    public void visit(WhileStmtNode node) {
        currentScope = new LocalScope("while","", currentScope);
        checkAndInitType(node.getCond());
        if(node.getCond().exprType == null || !node.getCond().getExprType().isBool()) {
            throw new ErrorHandler("error expression in \"while\"'s condition", node.getPos());
        }
        check(node.getBody());
        currentScope = currentScope.getParentScope();
    }

    @Override
    public void visit(BoolLiteralNode node) {

    }

    @Override
    public void visit(IntLiteralNode node) {

    }

    @Override
    public void visit(NullLiteralNode node) {

    }

    @Override
    public void visit(StringLiteralNode node) {

    }

    @Override
    public void visit(BinaryOpNode node) {
        checkAndInitType(node.getLeft());
        checkAndInitType(node.getRight());

        //type use check, left value check
        boolean valid = false;
        Type leftType = node.getLeft().exprType;
        Type rightType = node.getRight().exprType;
        if (leftType == null){
            throw new ErrorHandler("Error type using or l-value needed", node.getPos());
        }

        switch (node.getOp()){
            case ASSIGN:
                if (( (leftType.equals(rightType))
                        || (leftType.isArray() && rightType == null)
                        || (!leftType.isBuildInType() && rightType == null) )
                        && node.getLeft().isLvalue() /*expression's left value*/){
                    valid = true;
                }
                break;
            case ADD: case LT: case EQ:
                if ((leftType.isInt() && rightType != null && rightType.isInt())
                        || (leftType.isString() && rightType.isString())){
                    valid = true;
                }
                break;
            case LOG_OR: case LOG_AND:
                if (leftType.isBool() && rightType != null && rightType.isBool()) {
                    valid = true;
                }
                break;

                default:
                    if (leftType.isInt() && rightType != null &&rightType.isInt()){
                        valid = true;
                    }

        }
        if (!valid) {
            throw new ErrorHandler("Error type using or l-value needed", node.getPos());
        }

    }

    @Override
    public void visit(UnaryNode node) {
        checkAndInitType(node.getBody());
        //type use check, left value check
        boolean valid = false;
        Type type = node.getBody().exprType;
        if (type == null){
            throw new ErrorHandler("Error type using or l-value needed", node.getPos());
        }
        switch (node.getOp()){
            case DEC: case INC:
                if (type.isInt() && node.getBody().isLvalue()){
                    valid = true;
                }
                break;
            case NEG: case POS: case BIT_NOT:
                if (type.isInt()){
                    valid = true;
                }
                break;
            case LOG_NOT:
                if (type.isBool()){
                    valid = true;
                }
                break;
                default:
        }
        if (!valid)  throw new ErrorHandler("Error type using or l-value needed", node.getPos());
    }

    @Override
    public void visit(SuffixNode node) {
        checkAndInitType(node.getBody());
        //type use check, left value check
        Type type = node.getBody().exprType;
        if (type == null) throw new ErrorHandler("Error type using or l-value needed", node.getPos());
        if (type.isInt() && node.getBody().isLvalue()){ }
        else throw new ErrorHandler("Error type using or l-value needed", node.getPos());
    }

    @Override
    public void visit(ArrayIndexNode node) {
        checkAndInitType(node.getArray());
        checkAndInitType(node.getIndex());
        //there is nothing to do
    }

    @Override
    public void visit(VariableNode node) {
        DeclNode entity = currentScope.resolve(node.getName());
        if (entity != null){
            if (entity instanceof VarDeclNode) node.setValueDefinition( (VarDeclNode) entity);
            else throw new ErrorHandler("Undefined variable \"" + node.getName() + "\"" , node.getPos());
        }
        else{
            throw new ErrorHandler("Undefined variable \"" + node.getName() + "\"", node.getPos());
        }
    }

    @Override
    public void visit(ThisNode node) {
        DeclNode entity = currentScope.resolveThis();
        //must not be null!!
        if (entity != null) {
            node.setThisDefinition((ClassDeclNode) entity);
        }
        else throw new ErrorHandler("Error using of \"This\"", node.getPos());
    }

    @Override
    public void visit(CallNode node) {
        DeclNode decl = currentScope.resolve(node.getFuncName());
        if (decl!= null){
            if (decl instanceof FuncDeclNode){
                //connecting the entities
                node.setFuncDefinition((FuncDeclNode) decl);
            }
            else throw new ErrorHandler("Error use of entities \"" + node.getFuncName() + "\"" , node.getPos());
        }
        else{
            throw new ErrorHandler("Undefined function \"" + node.getFuncName() + "\"" , node.getPos());
        }

        //function params check;
        checkFuncParams(node.getFuncParams(), node.getFuncDefinition(), node.getPos());
    }

    @Override
    public void visit(MemberAccessNode node) {
        //special judge this!!
        checkAndInitType(node.getObject());

        LocalScope classScope = (LocalScope) tmpPreprocessScope.get(node.getObject().exprType.getTypeName());
        DeclNode varEntity = classScope.entities.get(node.getMemberRef());
        if (varEntity instanceof VarDeclNode){
            //bind
            node.setValueDefinition((VarDeclNode) varEntity);
            return;
        }
        throw new ErrorHandler("no such member!", node.getPos());
        // if i have the type of the object, then i know how to bind the
        // !!unfinished
        // not able to eliminate the reference of member access
    }

    @Override
    public void visit(MethodAccessNode node) {
        checkAndInitType(node.getObject());
        /*for (ExprNode exprNode : node.getMethodParams()) {
            checkAndInitType(exprNode);
        }*/
        LocalScope classScope = (LocalScope) tmpPreprocessScope.get(node.getObject().exprType.getTypeName());
        DeclNode methodEntity = classScope.entities.get(node.getMethodName());

        if (methodEntity instanceof FuncDeclNode){
            //bind
            node.setFuncDefinition((FuncDeclNode) methodEntity);
            checkFuncParams(node.getMethodParams(),node.getFuncDefinition(), node.getPos());
            return;
        }

        //now it is the buildin "size()"
        if (node.getMethodName().equals("size")){
            if (node.getMethodParams().size() != 0) throw new ErrorHandler("Unmatched function params type", node.getPos());
            if (!node.getObject().exprType.isArray()) throw new ErrorHandler("Invalild use of \"size\"", node.getPos());
            return;
        }

        throw new ErrorHandler("no such method!", node.getPos());
        // !!unfinished
        // not able to eliminate the reference of class method
    }

    @Override
    public void visit(NewNode node) {
        //already connect the type
        checkTypeDefinition(node.getExprType(),node.getPos());
        for (ExprNode exprNode : node.getExprDim()) {
            checkAndInitType(exprNode);
        }
    }
}
