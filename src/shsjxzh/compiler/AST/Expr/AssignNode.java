package shsjxzh.compiler.AST.Expr;

import shsjxzh.compiler.AST.ASTVisitor;
import shsjxzh.compiler.AST.tool.Position;

public class AssignNode extends ExprNode{
    private ExprNode left, right;

    public AssignNode(Position pos, ExprNode left, ExprNode right) {
        super(pos);
        this.left = left;
        this.right = right;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        super.accept(visitor);
    }
}
