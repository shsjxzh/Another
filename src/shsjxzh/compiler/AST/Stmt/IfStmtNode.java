package shsjxzh.compiler.AST.Stmt;

import shsjxzh.compiler.AST.ASTVisitor;
import shsjxzh.compiler.AST.Expr.ExprNode;
import shsjxzh.compiler.AST.tool.Position;

import java.util.List;

public class IfStmtNode extends StmtNode {
    private ExprNode cond;
    private StmtNode then;
    private StmtNode otherwise;

    public IfStmtNode(Position pos, ExprNode cond, StmtNode then, StmtNode otherwise) {
        super(pos);
        this.cond = cond;
        this.then = then;
        this.otherwise = otherwise;
    }

    public ExprNode getCond() {
        return cond;
    }

    public StmtNode getThen() {
        return then;
    }

    public StmtNode getOtherwise() {
        return otherwise;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
