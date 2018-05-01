package shsjxzh.compiler.UnUse;

import shsjxzh.compiler.AST.ASTVisitor;
import shsjxzh.compiler.AST.Expr.ExprNode;
import shsjxzh.compiler.AST.Expr.UnaryNode;
import shsjxzh.compiler.AST.tool.Position;

public class PerfixNode extends UnaryNode {
    public PerfixNode(Position pos, UnaryOp op, ExprNode body) {
        super(pos, op, body);
    }

    @Override
    public void accept(ASTVisitor visitor) {
        super.accept(visitor);
    }
}
