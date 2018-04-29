package shsjxzh.compiler.AST.Expr;

import shsjxzh.compiler.AST.ASTVisitor;
import shsjxzh.compiler.AST.tool.Position;

public class NullLiteralNode extends LiteralNode{
    public NullLiteralNode(Position pos) {
        super(pos);
        //this.exprTypeRef = "null";
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
