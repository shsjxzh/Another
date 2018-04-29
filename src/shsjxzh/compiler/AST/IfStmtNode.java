package shsjxzh.compiler.AST;

import shsjxzh.compiler.AST.Expr.ExprNode;
import shsjxzh.compiler.AST.tool.Position;

import java.util.List;

public class IfStmtNode extends StmtNode {
    private ExprNode cond;
    private StmtNode ifBody;
    private List<StmtNode> elseIfBody;
    private StmtNode elseBody;

    public IfStmtNode(Position pos, ExprNode cond, StmtNode ifBody) {
        super(pos);
        this.cond = cond;
        this.ifBody = ifBody;
    }

    public void setElseIfBody(List<StmtNode> elseIfBody) {
        this.elseIfBody = elseIfBody;
    }

    public void setElseBody(StmtNode elseBody) {
        this.elseBody = elseBody;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        super.accept(visitor);
    }
}
