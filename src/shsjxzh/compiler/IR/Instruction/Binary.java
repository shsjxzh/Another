package shsjxzh.compiler.IR.Instruction;

import shsjxzh.compiler.IR.BasicBlock;
import shsjxzh.compiler.IR.IRVisitor;
import shsjxzh.compiler.IR.Value.Register;
import shsjxzh.compiler.IR.Value.Value;

public class Binary extends Instruction {
    public enum BinaryOp{
        Add, Sub, Mul, Div, Mod, Shl, Shr,
        BitAnd/*(bitwise)*/, BitOr/*(bitwise)*/, Xor
    }

    private BinaryOp op;
    private Register dest;
    private Value left;
    private Value right;

    public Binary(BasicBlock curBB, BinaryOp op, Register dest, Value left, Value right) {
        super(curBB);
        this.op = op;
        this.dest = dest;
        this.left = left;
        this.right = right;
    }

    public BinaryOp getOp() {
        return op;
    }

    public Register getDest() {
        return dest;
    }

    public Value getLeft() {
        return left;
    }

    public Value getRight() {
        return right;
    }

    @Override
    public void accept(IRVisitor visitor) {
        visitor.visit(this);
    }
}
