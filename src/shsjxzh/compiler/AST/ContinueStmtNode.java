package shsjxzh.compiler.AST;

import shsjxzh.compiler.AST.tool.Position;

public class ContinueStmtNode extends StmtNode{
    public ContinueStmtNode(Position pos) {
        super(pos);
    }

    @Override
    public void accept(ASTVisitor visitor) {
        super.accept(visitor);
    }
}
