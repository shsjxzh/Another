package shsjxzh.compiler.AST.Expr;

import shsjxzh.compiler.AST.ASTVisitor;
import shsjxzh.compiler.AST.tool.Position;

public class OrNode extends BinaryOpNode{

    public OrNode(Position pos, ExprNode left, ExprNode right, BinaryOp op) {
        super(pos, left, right, op);
    }

    @Override
    public void accept(ASTVisitor visitor) {
        super.accept(visitor);
    }
}
