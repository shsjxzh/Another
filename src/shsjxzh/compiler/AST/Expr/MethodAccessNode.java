package shsjxzh.compiler.AST.Expr;

import shsjxzh.compiler.AST.ASTVisitor;
import shsjxzh.compiler.AST.tool.Position;

public class MethodAccessNode extends ExprNode {
    ExprNode object;
    CallNode method;

    public MethodAccessNode(Position pos, ExprNode object, CallNode method) {
        super(pos);
        this.object = object;
        this.method = method;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        super.accept(visitor);
    }
}
