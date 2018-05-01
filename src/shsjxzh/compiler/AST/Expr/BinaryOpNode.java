package shsjxzh.compiler.AST.Expr;

import shsjxzh.compiler.AST.ASTVisitor;
import shsjxzh.compiler.AST.tool.Position;

public class BinaryOpNode extends ExprNode{
    public enum BinaryOp {
        ASSIGN,
        LOG_OR, LOG_AND,
        BIT_OR, BIT_AND, XOR,
        EQ, NEQ, LT, GT, LE, GE,
        L_SHIFT, R_SHIFT,
        ADD, SUB,
        MUL, DIV, MOD
    }

    private ExprNode left, right;
    private BinaryOp op;

    public BinaryOpNode(Position pos, ExprNode left, ExprNode right, BinaryOp op) {
        super(pos);
        this.left = left;
        this.right = right;
        this.op = op;
    }

    public ExprNode getLeft() {
        return left;
    }

    public ExprNode getRight() {
        return right;
    }

    public BinaryOp getOp() {
        return op;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
