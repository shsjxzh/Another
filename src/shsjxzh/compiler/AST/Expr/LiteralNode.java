package shsjxzh.compiler.AST.Expr;

import shsjxzh.compiler.AST.ASTVisitor;
import shsjxzh.compiler.AST.tool.Position;

public abstract class LiteralNode extends ExprNode {
    public LiteralNode(Position pos) {
        super(pos);
    }

    @Override
    public abstract void accept(ASTVisitor visitor) ;
}
