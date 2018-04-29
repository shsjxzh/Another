package shsjxzh.compiler.FrontEnd;

import shsjxzh.compiler.AST.*;
import shsjxzh.compiler.AST.Decl.ClassDeclNode;
import shsjxzh.compiler.AST.Decl.DeclNode;
import shsjxzh.compiler.AST.Decl.FuncDeclNode;
import shsjxzh.compiler.AST.Decl.VarDeclNode;
import shsjxzh.compiler.AST.Expr.*;
import shsjxzh.compiler.AST.Stmt.*;
import shsjxzh.compiler.UnUse.AndNode;
import shsjxzh.compiler.UnUse.AssignNode;
import shsjxzh.compiler.UnUse.OrNode;
import shsjxzh.compiler.UnUse.SuffixNode;

import java.io.PrintStream;
import java.util.List;

public class ASTPrinter implements ASTVisitor {
    private String indent;
    private PrintStream out;

    public ASTPrinter(PrintStream out) {
        indent = "";
        this.out = out;
    }

    //下面是工具函数
    private void addIndent() {indent += "\t";}
    private void subIndent() {indent = indent.substring(1);}
    private void myPrint(String s) {this.out.print(indent + s);}
    private void myPrintln(String s) {this.out.println(indent + s);}

    @Override
    public void visit(ProgramNode node) {
        myPrintln("ProgramNode:");
        List<DeclNode> decls = node.getDeclnodes();
        for (DeclNode decl : decls) {
            decl.accept(this);
        }
    }

    @Override
    public void visit(FuncDeclNode node) {
        addIndent();
        myPrintln("FuncDeclNode:");
        subIndent();
    }

    @Override
    public void visit(ClassDeclNode node) {

    }


    @Override
    public void visit(VarDeclNode node) {
        addIndent();
        myPrintln("VarDeclNode :");
        myPrintln("name: " + node.getVarName());
        node.getVarType().accept(this);
        if (node.getExpr() != null){
            node.getExpr().accept(this);
        }
        subIndent();
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
    public void visit(VarDeclStmtNode node) {

    }

    @Override
    public void visit(ReturnStmtNode node) {

    }

    @Override
    public void visit(WhileStmtNode node) {

    }

    @Override
    public void visit(LiteralNode node) {

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
    public void visit(AndNode node) {

    }

    @Override
    public void visit(OrNode node) {

    }

    @Override
    public void visit(UnaryNode node) {

    }

    @Override
    public void visit(SuffixNode node) {

    }

    @Override
    public void visit(PerfixNode node) {

    }

    @Override
    public void visit(AssignNode node) {

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
    public void visit(TypeNode node) {
        addIndent();
        myPrintln("TypeNode:");
        myPrintln(node.getType().getDetail());
    }
}
