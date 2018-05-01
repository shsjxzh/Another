package shsjxzh.compiler.AST.Stmt;

import shsjxzh.compiler.AST.ASTVisitor;
import shsjxzh.compiler.AST.Stmt.StmtNode;
import shsjxzh.compiler.AST.tool.Position;

import java.util.List;

public class BlockNode extends StmtNode {
    protected List<StmtNode> stmt;

    public BlockNode(Position pos, List<StmtNode> stmt) {
        super(pos);
        this.stmt = stmt;
    }

    public List<StmtNode> getStmt() {
        return stmt;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
