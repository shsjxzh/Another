package shsjxzh.compiler.AST;

import shsjxzh.compiler.AST.Expr.ExprNode;
import shsjxzh.compiler.AST.tool.Position;

public class ForStmtNode extends StmtNode{
    private ExprNode begin_expr;
    private VarDeclNode begin_varDecl;
    private ExprNode cond;
    private ExprNode iter;
    private StmtNode body;

    public ForStmtNode(Position pos, ExprNode begin_expr, ExprNode cond, ExprNode iter, StmtNode body) {
        super(pos);
        this.begin_expr = begin_expr;
        this.cond = cond;
        this.iter = iter;
        this.body = body;
    }

    public ForStmtNode(Position pos, VarDeclNode begin_varDecl, ExprNode cond, ExprNode iter, StmtNode body) {
        super(pos);
        this.begin_varDecl = begin_varDecl;
        this.cond = cond;
        this.iter = iter;
        this.body = body;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        super.accept(visitor);
    }
}
