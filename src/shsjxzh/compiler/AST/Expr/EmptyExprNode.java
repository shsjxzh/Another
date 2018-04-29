package shsjxzh.compiler.AST.Expr;

import shsjxzh.compiler.AST.ASTVisitor;
import shsjxzh.compiler.AST.tool.Position;

public class EmptyExprNode extends ExprNode {
    public EmptyExprNode(Position pos) {
        super(pos);
    }

    @Override
    public void accept(ASTVisitor visitor) {
        super.accept(visitor);
    }
}
