package shsjxzh.compiler.AST;

import shsjxzh.compiler.AST.tool.Position;

public class DeclNode extends ASTNode{
    public DeclNode(Position pos) {
        super(pos);
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
