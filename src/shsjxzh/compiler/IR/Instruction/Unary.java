package shsjxzh.compiler.IR.Instruction;

import shsjxzh.compiler.IR.BasicBlock;
import shsjxzh.compiler.IR.IRVisitor;
import shsjxzh.compiler.IR.Value.Register;
import shsjxzh.compiler.IR.Value.Value;

public class Unary extends Instruction {
    public enum UnaryOp{
        Neg, BitNot/*(bitwise)*/
    }

    private UnaryOp op;
    private Register dest;

    public Unary(BasicBlock belongBB, UnaryOp op, Register dest) {
        super(belongBB);
        this.op = op;
        this.dest = dest;
    }

    public UnaryOp getOp() {
        return op;
    }

    public Register getDest() {
        return dest;
    }

    @Override
    public void accept(IRVisitor visitor) {
        visitor.visit(this);
    }
}
