package shsjxzh.compiler.AST;

import shsjxzh.compiler.AST.Expr.ExprNode;
import shsjxzh.compiler.AST.tool.Position;

public class ExprStmtNode extends StmtNode{
    public ExprNode expr;

    public ExprStmtNode(Position pos, ExprNode expr) {
        super(pos);
        this.expr = expr;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        super.accept(visitor);
    }
}
