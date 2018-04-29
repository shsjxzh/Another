package shsjxzh.compiler.AST;

import shsjxzh.compiler.AST.Expr.ExprNode;
import shsjxzh.compiler.AST.tool.Position;

public class ReturnStmtNode extends StmtNode{
    private ExprNode reExpr;

    public ReturnStmtNode(Position pos, ExprNode reExpr) {
        super(pos);
        this.reExpr = reExpr;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        super.accept(visitor);
    }
}
