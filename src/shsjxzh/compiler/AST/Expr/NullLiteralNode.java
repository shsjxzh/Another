package shsjxzh.compiler.AST.Expr;

import shsjxzh.compiler.AST.ASTVisitor;
import shsjxzh.compiler.AST.tool.Position;
import shsjxzh.compiler.Type.Type;

public class NullLiteralNode extends LiteralNode{
    public NullLiteralNode(Position pos) {
        super(pos);
        //this.exprTypeRef = "null";
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void initExprType() {
        exprType = new Type("null", 0);
    }
}
