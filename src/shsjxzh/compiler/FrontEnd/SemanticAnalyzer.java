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

public class SemanticAnalyzer implements ASTVisitor{
    private GlobalScope globalScope;
    private Scope currentScope;
    private int blockNum, forNum, whileNum, ifNum;

    //the latest
    private Map<String, ClassDeclNode> typeDefinitions;

    public SemanticAnalyzer(){
        //aim:
        //check undefined variables and functions
        //right type using(operator, return type, function calls)
        globalScope = new GlobalScope();
        currentScope = globalScope;
        typeDefinitions = new HashMap<>();
        blockNum = 0;
        forNum = 0;
        whileNum = 0;
        ifNum = 0;
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
    }

    //check type reference
    private void checkTypeDefinition(Type type, Position pos) {
        if (!type.isNull()) {//type != null &&
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
                    || (leftType.isArray() && rightType.isNull())
                    || (!leftType.isBuildInType() && rightType.isNull()) ){}
                    else{
                throw new ErrorHandler("Unmatched function params type", exprNode.getPos());
            }
            ++i;
        }
    }

    private void preProcessFunc(FuncDeclNode node) {
        if (node != null) {
            currentScope.define(node);
            currentScope = new LocalScope("function", node.getName(), currentScope);
            ((LocalScope) currentScope).parent.childScope.put(currentScope.getName(), currentScope);
            checkTypeDefinition(node.getFuncReturnType(), node.getPos());
            for (VarDeclNode varDeclNode : node.getFuncParams()) {
                check(varDeclNode);
            }

            currentScope = currentScope.getParentScope();
        }
    }
    private void preProssClass(ClassDeclNode node){
        currentScope.define(node);
        currentScope = new LocalScope("class", node.getName(),currentScope);
       ((LocalScope) currentScope).parent.childScope.put(currentScope.getName(), currentScope);

        //define, not been checked yet
        for (VarDeclNode varDeclNode : node.getClassMember()) {
            check(varDeclNode);
        }
        for (FuncDeclNode funcDeclNode : node.getClassMethod()) {
            if (funcDeclNode.getName().equals("main")){
                throw new ErrorHandler("Error use of \"main\"", node.getPos());
            }
            preProcessFunc(funcDeclNode);
        }
        preProcessFunc(node.getConstructMethod());

        currentScope = currentScope.getParentScope();
    }

    @Override
    public void visit(ProgramNode node) {
        //preprocess for forward reference
        //first preprocess: define all the types
        //second preprocess: build all function and class scope for forward reference
        List<DeclNode> decls = node.getDeclnodes();
        for (DeclNode decl : decls) {
            if (decl instanceof ClassDeclNode) {
                typeDefinitions.put(decl.getName(), (ClassDeclNode) decl);
            }
        }

        DeclNode mainDecl = node.getMainDecl();

        if (mainDecl instanceof FuncDeclNode) preProcessFunc( (FuncDeclNode) mainDecl);
        for (DeclNode decl : decls) {
            if (decl instanceof FuncDeclNode){
                preProcessFunc((FuncDeclNode) decl);
            }
            if (decl instanceof ClassDeclNode){
                preProssClass((ClassDeclNode) decl);
            }
        }

        //main check
        if (mainDecl == null) throw new ErrorHandler("missing main function", node.getPos());
        else {
            if (mainDecl instanceof FuncDeclNode){
                if (!((FuncDeclNode) mainDecl).getFuncReturnType().isNull()
                        && ((FuncDeclNode) mainDecl).getFuncReturnType().isInt()) {}
                else throw new ErrorHandler("error main function", node.getPos());
            }
            else throw new ErrorHandler("error main function", node.getPos());
        }

        //check all
        check(mainDecl);
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
        currentScope = currentScope.childScope.get(node.getName());
        check(node.getFuncBlock());
        currentScope = currentScope.getParentScope();
        
    }

    @Override
    public void visit(ClassDeclNode node) {
        currentScope = currentScope.childScope.get(node.getName());
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
            if( (leftType.isArray() && rightType.isNull())
                    || (!leftType.isBuildInType() && rightType.isNull())
                    || leftType.equals(rightType)) {}
            else{
                throw new ErrorHandler("Unmatched type in value initialization",node.getPos());
            }
        }
        if (node.getName().equals("main")){
            throw new ErrorHandler("Error using of \"main\"", node.getPos());
        }
        currentScope.define(node);
        
    }

    @Override
    public void visit(VarDeclStmtNode node) {
        check(node.getVarDecl());
        
    }

    @Override
    public void visit(BlockNode node) {
        currentScope = new LocalScope("block","block" + (++blockNum), currentScope);
        ((LocalScope) currentScope).parent.childScope.put(currentScope.getName(),currentScope);
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
        currentScope = new LocalScope("for","for" + (++forNum), currentScope);
        ((LocalScope) currentScope).parent.childScope.put(currentScope.getName(),currentScope);
        checkAndInitType(node.getBegin_expr());
        check(node.getBegin_varDecl());
        checkAndInitType(node.getCond());
        //in for's condition, it can not be anything
        if (node.getCond() != null && !node.getCond().exprType.isBool()){
            throw new ErrorHandler("Error expression in \"for\"",node.getCond().getPos());
        }
        checkAndInitType(node.getIter());
        check(node.getBody());
        currentScope = currentScope.getParentScope();
        
    }

    @Override
    public void visit(IfStmtNode node) {
        currentScope = new LocalScope("if","if"+(++ifNum), currentScope);
        ((LocalScope) currentScope).parent.childScope.put(currentScope.getName(),currentScope);
        checkAndInitType(node.getCond());
        //double check this!
        /*if(node.getCond().exprType == null || !node.getCond().getExprType().isBool() ){
            throw new ErrorHandler("error expression in \"if\"'s condition", node.getPos());
        }*/
        if(node.getCond() == null || !node.getCond().getExprType().isBool() ){
            throw new ErrorHandler("error expression in \"if\"'s condition", node.getPos());
        }

        check(node.getThen());
        check(node.getOtherwise());
        currentScope = currentScope.getParentScope();
        
    }

    @Override
    public void visit(ReturnStmtNode node) {
        checkAndInitType(node.getReExpr());
        if (!currentScope.resolveReturn(node)){
            throw new ErrorHandler("Error return type", node.getPos());
        }
        
    }

    @Override
    public void visit(WhileStmtNode node) {
        currentScope = new LocalScope("while","while"+(++whileNum), currentScope);
        ((LocalScope) currentScope).parent.childScope.put(currentScope.getName(),currentScope);
        checkAndInitType(node.getCond());
        if(node.getCond() == null || !node.getCond().getExprType().isBool()) {
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

        //check leftType
        if (leftType.isNull()){
            if (node.getOp() == BinaryOpNode.BinaryOp.ASSIGN){
                if (rightType.isNull())  throw new ErrorHandler("Error type using or l-value needed", node.getPos());
                else if (rightType.isBuildInType() || !rightType.isArray()) throw new ErrorHandler("Error using of \"==\"", node.getPos());
            }
            else throw new ErrorHandler("Error type using or l-value needed", node.getPos());
        }

        switch (node.getOp()){
            case ASSIGN:
                if (( (leftType.equals(rightType))
                        || (leftType.isArray() && rightType.isNull())
                        || (!leftType.isBuildInType() && rightType.isNull()) )
                        && node.getLeft().isLvalue() /*expression's left value*/){
                    valid = true;
                }
                break;
            case ADD: case LT: case LE: case GT: case GE:
                if ((leftType.isInt() && !rightType.isNull() && rightType.isInt())
                        || (leftType.isString() && rightType.isString())){
                    valid = true;
                }
                break;
            case LOG_OR: case LOG_AND:
                if (leftType.isBool() && !rightType.isNull() && rightType.isBool()) {
                    valid = true;
                }
                break;
            case EQ: case NEQ:
                if ((leftType.isInt() && !rightType.isNull() && rightType.isInt())
                        || (leftType.isBool() && rightType.isBool())
                        || (leftType.isString() && rightType.isString())
                        || ((!leftType.isBuildInType() || leftType.isArray()) && rightType.isNull() )){
                    valid = true;
                }

                default:
                    if (leftType.isInt() && !rightType.isNull() &&rightType.isInt()){
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
        if (type.isNull()){
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
        if (type.isNull()) throw new ErrorHandler("Error type using or l-value needed", node.getPos());
        if (type.isInt() && node.getBody().isLvalue()){ }
        else throw new ErrorHandler("Error type using or l-value needed", node.getPos());
    }

    @Override
    public void visit(ArrayIndexNode node) {
        checkAndInitType(node.getArray());
        checkAndInitType(node.getIndex());
        if (!node.getIndex().getExprType().isInt()) throw new ErrorHandler("the dim must be int type!",node.getIndex().getPos());
    }

    @Override
    public void visit(VariableNode node) {
        DeclNode entity = currentScope.resolve(node.getName());
        if (entity != null){
            if (entity instanceof VarDeclNode) {
                node.setValueDefinition( (VarDeclNode) entity);
            }
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

        //no call main
        if (decl.getName().equals("main")){
            throw new ErrorHandler("main function cannot be called in the programme", node.getPos());
        }

        //function params check;
        checkFuncParams(node.getFuncParams(), node.getFuncDefinition(), node.getPos());
        
    }

    @Override
    public void visit(MemberAccessNode node) {
        //special judge this!!
        checkAndInitType(node.getObject());

        LocalScope classScope = (LocalScope) globalScope.childScope.get(node.getObject().exprType.getTypeName());
        DeclNode varEntity = classScope.entities.get(node.getMemberRef());
        if (varEntity instanceof VarDeclNode){
            //bind
            node.setValueDefinition((VarDeclNode) varEntity);
            return;
        }
        throw new ErrorHandler("no such member!", node.getPos());
    }

    @Override
    public void visit(MethodAccessNode node) {
        checkAndInitType(node.getObject());
        LocalScope classScope = (LocalScope) globalScope.childScope.get(node.getObject().exprType.getTypeName());
        DeclNode methodEntity = classScope.entities.get(node.getMethodName());

        if (methodEntity instanceof FuncDeclNode){
            //bind
            node.setFuncDefinition((FuncDeclNode) methodEntity);
            checkFuncParams(node.getMethodParams(),node.getFuncDefinition(), node.getPos());
            return;
        }

        //the self defined "size()" has been handled, now it is the buildin "size()"
        if (node.getMethodName().equals("size")){
            if (node.getMethodParams().size() != 0) throw new ErrorHandler("Unmatched function params type", node.getPos());
            if (!node.getObject().exprType.isArray()) throw new ErrorHandler("Invalild use of \"size\"", node.getPos());
            return;
        }

        throw new ErrorHandler("no such method!", node.getPos());
    }

    @Override
    public void visit(NewNode node) {
        checkTypeDefinition(node.getExprType(),node.getPos());
        for (ExprNode exprNode : node.getExprDim()) {
            checkAndInitType(exprNode);
            if (!exprNode.getExprType().isInt()) throw new ErrorHandler("the dim must be int type!",exprNode.getPos());
        }
    }
}
