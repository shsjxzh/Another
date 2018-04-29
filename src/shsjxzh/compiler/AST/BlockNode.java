package shsjxzh.compiler.AST;

import shsjxzh.compiler.AST.tool.Position;

import java.util.List;

public class BlockNode extends StmtNode{
    protected List<StmtNode> stmt;

    public BlockNode(Position pos, List<StmtNode> stmt) {
        super(pos);
        this.stmt = stmt;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
