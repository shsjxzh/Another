package shsjxzh.compiler.FrontEnd;

import shsjxzh.compiler.AST.ASTVisitor;
import shsjxzh.compiler.AST.Decl.ClassDeclNode;
import shsjxzh.compiler.AST.Decl.FuncDeclNode;
import shsjxzh.compiler.AST.Decl.VarDeclNode;
import shsjxzh.compiler.AST.Expr.*;
import shsjxzh.compiler.AST.ProgramNode;
import shsjxzh.compiler.AST.Stmt.*;
import shsjxzh.compiler.IR.BasicBlock;

public class IRBuilder implements ASTVisitor {
    BasicBlock curBB;


    @Override
    public void visit(ProgramNode node) {

    }

    @Override
    public void visit(FuncDeclNode node) {

    }

    @Override
    public void visit(ClassDeclNode node) {

    }

    @Override
    public void visit(VarDeclNode node) {

    }

    @Override
    public void visit(VarDeclStmtNode node) {

    }

    @Override
    public void visit(BlockNode node) {

    }

    @Override
    public void visit(BreakStmtNode node) {

    }

    @Override
    public void visit(ContinueStmtNode node) {

    }

    @Override
    public void visit(ExprStmtNode node) {

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

    }

    @Override
    public void visit(UnaryNode node) {

    }

    @Override
    public void visit(SuffixNode node) {

    }

    @Override
    public void visit(ArrayIndexNode node) {

    }

    @Override
    public void visit(VariableNode node) {

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
}
