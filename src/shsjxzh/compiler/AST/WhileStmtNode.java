package shsjxzh.compiler.AST;

import shsjxzh.compiler.AST.Expr.ExprNode;
import shsjxzh.compiler.AST.tool.Position;

public class WhileStmtNode extends StmtNode{
    private ExprNode cond;
    private StmtNode body;

    public WhileStmtNode(Position pos, ExprNode cond, StmtNode body) {
        super(pos);
        this.cond = cond;
        this.body = body;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        super.accept(visitor);
    }
}
