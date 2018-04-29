package shsjxzh.compiler.AST.Stmt;

import shsjxzh.compiler.AST.ASTVisitor;
import shsjxzh.compiler.AST.Expr.ExprNode;
import shsjxzh.compiler.AST.tool.Position;

import java.util.List;

public class IfStmtNode extends StmtNode {
    private ExprNode ifCond;
    private StmtNode ifBody;
    private List<ExprNode> elseIfcond;
    private List<StmtNode> elseIfBody;
    private StmtNode elseBody;

    public IfStmtNode(Position pos, ExprNode ifCond, StmtNode ifBody, List<ExprNode> elseIfcond, List<StmtNode> elseIfBody, StmtNode elseBody) {
        super(pos);
        this.ifCond = ifCond;
        this.ifBody = ifBody;
        this.elseIfcond = elseIfcond;
        this.elseIfBody = elseIfBody;
        this.elseBody = elseBody;
    }

    public void setElseIfBody(List<StmtNode> elseIfBody) {
        this.elseIfBody = elseIfBody;
    }

    public void setElseBody(StmtNode elseBody) {
        this.elseBody = elseBody;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
