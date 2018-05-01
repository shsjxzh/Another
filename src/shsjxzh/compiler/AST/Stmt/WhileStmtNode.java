package shsjxzh.compiler.AST.Stmt;

import shsjxzh.compiler.AST.ASTVisitor;
import shsjxzh.compiler.AST.Expr.ExprNode;
import shsjxzh.compiler.AST.Stmt.StmtNode;
import shsjxzh.compiler.AST.tool.Position;

public class WhileStmtNode extends StmtNode {
    private ExprNode cond;
    private StmtNode body;

    public WhileStmtNode(Position pos, ExprNode cond, StmtNode body) {
        super(pos);
        this.cond = cond;
        this.body = body;
    }

    public ExprNode getCond() {
        return cond;
    }

    public StmtNode getBody() {
        return body;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
