package shsjxzh.compiler.AST.Expr;

import shsjxzh.compiler.AST.ASTVisitor;
import shsjxzh.compiler.AST.tool.Position;

public class MemberAccessNode extends ExprNode{
    ExprNode object;
    ExprNode member;

    public MemberAccessNode(Position pos, ExprNode object, ExprNode member) {
        super(pos);
        this.object = object;
        this.member = member;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        super.accept(visitor);
    }
}
