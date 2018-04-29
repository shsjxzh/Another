package shsjxzh.compiler.AST.Expr;

import shsjxzh.compiler.AST.ASTVisitor;
import shsjxzh.compiler.AST.tool.Position;

public class MemberAccessNode extends ExprNode{
    ExprNode object;
    String memberRef;

    public MemberAccessNode(Position pos, ExprNode object, String memberRef) {
        super(pos);
        this.object = object;
        this.memberRef = memberRef;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
