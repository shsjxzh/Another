package shsjxzh.compiler.AST;

import shsjxzh.compiler.AST.tool.Position;

public class StmtNode extends ASTNode{
    public StmtNode(Position pos) {
        super(pos);
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
