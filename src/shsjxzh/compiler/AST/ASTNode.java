package shsjxzh.compiler.AST;

import shsjxzh.compiler.AST.tool.Position;

public abstract class ASTNode {
    protected  Position pos;

    public ASTNode(Position pos) {
        this.pos = pos;
    }

    public abstract void accept(ASTVisitor visitor);
}
