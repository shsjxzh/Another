package shsjxzh.compiler.IR.Instruction;

import shsjxzh.compiler.IR.BasicBlock;
import shsjxzh.compiler.IR.IRVisitor;
import shsjxzh.compiler.IR.Value.Register;
import shsjxzh.compiler.IR.Value.Value;

public class Unary extends Instruction {
    public enum UnaryOp{
        Neg, Not/*(bitwise)*/, Inc, Dec
    }

    private UnaryOp op;
    private Register dest;
    private Value value;

    public Unary(BasicBlock curBB, UnaryOp op, Register dest, Value value) {
        super(curBB);
        this.op = op;
        this.dest = dest;
        this.value = value;
    }

    @Override
    public void accept(IRVisitor visitor) {
        visitor.visit(this);
    }
}
