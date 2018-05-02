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

import java.util.List;
import java.util.Map;

public class ReferenceResolver implements ASTVisitor {
    private GlobalScope globalScope;
    private Scope currentScope;

    //preprocess class
    private Map<String, Scope> tmpClassScope;
    private Map<String, ClassDeclNode> typeDefinitions;

    public ReferenceResolver(){
        //check undefined variables and functions
        globalScope = new GlobalScope();
        currentScope = globalScope;
    }

    private void check(ASTNode node) {
        if (node != null) {
            node.accept(this);
        }
    }

    //type reference
    private void checkType(Type type, Position pos) {
        if (type != null) {
            ClassDeclNode typeDefinition = typeDefinitions.get(type.getTypeName());
            if (typeDefinition == null) {
                throw new ErrorHandler("Undefined type", pos);
            }
            type.setTypeDefinition(typeDefinition);
        }
    }

    public GlobalScope getGlobalScope() {
        return globalScope;
    }

    private void preProssClass(ClassDeclNode node){
        currentScope = new LocalScope("class", currentScope);
        tmpClassScope.put(node.getName(), currentScope);

        //define, not been checked yet
        for (VarDeclNode varDeclNode : node.getClassMember()) {
            currentScope.define(varDeclNode);
        }
        for (FuncDeclNode funcDeclNode : node.getClassMethod()) {
            currentScope.define(funcDeclNode);
        }
    }

    @Override
    public void visit(ProgramNode node) {
        //preprocess for forward reference
        List<DeclNode> decls = node.getDeclnodes();
        for (DeclNode decl : decls) {
            if (decl instanceof FuncDeclNode){
                //define with duplicate check
                currentScope.define(decl);
            }
            if (decl instanceof ClassDeclNode) {
                currentScope.define(decl);
                typeDefinitions.put(decl.getName(), (ClassDeclNode) decl);
                preProssClass((ClassDeclNode) decl);
            }
        }

        //check all
        for (DeclNode decl : decls) {
            check(decl);
        }
    }

    @Override
    public void visit(FuncDeclNode node) {
        //if (currentScope.getKind().equals("class")){
        //function & class method
        currentScope = new LocalScope("function", currentScope);
        if (node.getFuncReturnType() != null) {
            checkType(node.getFuncReturnType(), node.getPos());
        }
        for (VarDeclNode varDeclNode : node.getFuncParams()) {
            currentScope.define(varDeclNode);
        }
        check(node.getFuncBlock());
        //block will turn to the parent scope
        //}
    }

    @Override
    public void visit(ClassDeclNode node) {
        currentScope = tmpClassScope.get(node.getName());
        for (VarDeclNode varDeclNode : node.getClassMember()) {
            check(varDeclNode);
        }
    }

    @Override
    public void visit(VarDeclNode node) {

        checkType(node.getVarType(), node.getPos());
        check(node.getExpr());

        if (!currentScope.getKind().equals("class")) currentScope.define(node);
    }

    @Override
    public void visit(VarDeclStmtNode node) {
        check(node.getVarDecl());
    }

    @Override
    public void visit(BlockNode node) {
        //think about different cases
    }

    @Override
    public void visit(BreakStmtNode node) {

    }

    @Override
    public void visit(ContinueStmtNode node) {

    }

    @Override
    public void visit(ExprStmtNode node) {
        check(node.expr);
    }

    @Override
    public void visit(ForStmtNode node) {

    }

    @Override
    public void visit(IfStmtNode node) {

    }

    @Override
    public void visit(ReturnStmtNode node) {

    }

    @Override
    public void visit(WhileStmtNode node) {

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
        check(node.getLeft());
        check(node.getRight());

    }

    @Override
    public void visit(UnaryNode node) {
        check(node.getBody());
    }

    @Override
    public void visit(SuffixNode node) {
        check(node.getBody());
    }

    @Override
    public void visit(ArrayIndexNode node) {
        check(node.getArray());
        check(node.getIndex());
    }

    @Override
    public void visit(VariableNode node) {
        DeclNode entity = currentScope.resolve(node.getName());
        if (entity != null){
            node.setEntity(entity);
        }
        else{
            throw new ErrorHandler("Undefined variable" , node.getPos());
        }
    }

    @Override
    public void visit(ThisNode node) {
        //DeclNode entity = currentScope
    }

    @Override
    public void visit(CallNode node) {

    }

    @Override
    public void visit(MemberAccessNode node) {
        //special judge this!!
    }

    @Override
    public void visit(MethodAccessNode node) {

    }

    @Override
    public void visit(NewNode node) {

    }
}
