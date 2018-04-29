package shsjxzh.compiler.AST;

import shsjxzh.compiler.AST.Decl.ClassDeclNode;
import shsjxzh.compiler.AST.Decl.FuncDeclNode;
import shsjxzh.compiler.AST.Decl.VarDeclNode;
import shsjxzh.compiler.AST.Expr.*;
import shsjxzh.compiler.AST.Stmt.*;
import shsjxzh.compiler.UnUse.AndNode;
import shsjxzh.compiler.UnUse.AssignNode;
import shsjxzh.compiler.UnUse.OrNode;
import shsjxzh.compiler.UnUse.SuffixNode;
//import shsjxzh.compiler.Unuse.VarDeclStmtNode;

public interface ASTVisitor{

    void visit(ProgramNode node);

    //Decl
    //void visit(DeclNode node);
    void visit(FuncDeclNode node);
    void visit(ClassDeclNode node);
    void visit(VarDeclNode node);

    //Stmt
    //void visit(StmtNode node);
    void visit(VarDeclStmtNode node);
    void visit(BlockNode node);
    void visit(BreakStmtNode node);
    void visit(ContinueStmtNode node);
    void visit(ExprStmtNode node);
    void visit(ForStmtNode node);
    void visit(IfStmtNode node);
    void visit(ReturnStmtNode node);
    void visit(WhileStmtNode node);

    //Expr
    //void visit(ExprNode node);

    void visit(LiteralNode node);
    void visit(BoolLiteralNode node);
    void visit(IntLiteralNode node);
    void visit(NullLiteralNode node);
    void visit(StringLiteralNode node);

    void visit(BinaryOpNode node);
    void visit(AndNode node);
    void visit(OrNode node);

    void visit(UnaryNode node);
    void visit(SuffixNode node);
    void visit(PerfixNode node);

    void visit(AssignNode node);
    void visit(ArrayIndexNode node);
    void visit(VariableNode node);

    void visit(CallNode node);
    //void visit(ExprListNode node);

    void visit(MemberAccessNode node);
    void visit(MethodAccessNode node);

    void visit(NewNode node);

    //Type
    void visit(TypeNode node);
}
