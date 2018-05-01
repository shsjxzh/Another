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
import shsjxzh.compiler.AST.TypeNode;
import shsjxzh.compiler.ErrorHandle.ErrorHandler;
import shsjxzh.compiler.Scope.GlobalScope;
import shsjxzh.compiler.Scope.LocalScope;
import shsjxzh.compiler.Scope.Scope;

import java.util.List;
import java.util.Map;

public class ScopeTreePrePross implements ASTVisitor {
    private GlobalScope globalScope;
    private Scope currentScope;

    //下面这个仅仅是用来预处理class类型
    private Map<String, Scope> tmpClassScope;
    private Map<String, ClassDeclNode> typeDefinitions;

    public ScopeTreePrePross(){
        //目标：检查未定义变量和未定义函数

        globalScope = new GlobalScope();
        currentScope = globalScope;

        //留一段空间给类型检查用


    }

    private void check(ASTNode node){
        node.accept(this);
    }

    public GlobalScope getGlobalScope() {
        return globalScope;
    }

    private void preProssClass(ClassDeclNode node){
        currentScope = new LocalScope("class", currentScope);
        tmpClassScope.put(node.getName(), currentScope);

        //这里仅仅是将变量和函数声明了一下，还没有检验
        for (VarDeclNode varDeclNode : node.getClassMember()) {
            currentScope.define(varDeclNode);
        }
        for (FuncDeclNode funcDeclNode : node.getClassMethod()) {
            currentScope.define(funcDeclNode);
        }
    }

    @Override
    public void visit(ProgramNode node) {
        //先进行预处理来保证函数、类的向前引用
        List<DeclNode> decls = node.getDeclnodes();
        for (DeclNode decl : decls) {
            if (decl instanceof FuncDeclNode){
                //define专指define，已经带了重名检查
                //注意define仅仅是将这个node加入了当前作用域的列表中，该节点本身并没有经过检查
                currentScope.define(decl);
            }

            if (decl instanceof ClassDeclNode) {
                currentScope.define(decl);
                preProssClass((ClassDeclNode) decl);
            }
        }

        for (DeclNode decl : decls) {
            // 让这棵树继续访问其他人
            // 同时完成引用消除和类型消除
            check(decl);
        }
    }

    @Override
    public void visit(FuncDeclNode node) {
        if (currentScope.getKind().equals("class")){

        }
        else {
            //全局函数
            currentScope = new LocalScope("function", currentScope);
            //假定typeNode能够完成消除类型引用并检查的任务
            if (node.getFuncReturnType() != null) {
                check(node.getFuncReturnType());
            }
            for (VarDeclNode varDeclNode : node.getFuncParams()) {
                currentScope.define(varDeclNode);
            }
            check(node.getFuncBlock());
            //不需要退出作用域，block会替你做这件事
        }
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
        //只针对正常的
        //变量的类型消除
        check(node.getVarType());

        //对可能的表达式进行引用消除
        if (node.getExpr() != null) check(node.getExpr());

        if (!currentScope.getKind().equals("class")) currentScope.define(node);
    }

    @Override
    public void visit(VarDeclStmtNode node) {
        check(node.getVarDecl());
    }

    @Override
    public void visit(BlockNode node) {
        //检查几种情况
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
    public void visit(CallNode node) {

    }

    @Override
    public void visit(MemberAccessNode node) {

    }

    @Override
    public void visit(MethodAccessNode node) {

    }

    @Override
    public void visit(NewNode node) {

    }

    @Override
    public void visit(ThisNode node) {

    }

    @Override
    public void visit(TypeNode node) {

    }
}
