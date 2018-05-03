package shsjxzh.compiler.AST.Expr;

import shsjxzh.compiler.AST.ASTVisitor;
import shsjxzh.compiler.AST.tool.Position;

public class UnaryNode extends ExprNode {
    public enum UnaryOp {
        INC, DEC, POS, NEG, LOG_NOT, BIT_NOT
    }
    public UnaryOp op;
    public ExprNode body;

    public UnaryNode(Position pos, UnaryOp op, ExprNode body) {
        super(pos);
        this.op = op;
        this.body = body;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }

    public UnaryOp getOp() {
        return op;
    }

    public ExprNode getBody() {
        return body;
    }

    @Override
    public void initExprType() {
        exprType = body.getExprType();
    }
}
