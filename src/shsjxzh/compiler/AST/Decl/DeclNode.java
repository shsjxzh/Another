package shsjxzh.compiler.AST.Decl;

import shsjxzh.compiler.AST.ASTNode;
import shsjxzh.compiler.AST.ASTVisitor;
import shsjxzh.compiler.AST.tool.Position;

public abstract class DeclNode extends ASTNode {
    public DeclNode(Position pos) {
        super(pos);
    }

    @Override
    public abstract void accept(ASTVisitor visitor);
}
