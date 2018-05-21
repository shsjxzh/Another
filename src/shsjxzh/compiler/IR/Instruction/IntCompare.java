package shsjxzh.compiler.IR.Instruction;

import shsjxzh.compiler.IR.BasicBlock;
import shsjxzh.compiler.IR.IRVisitor;
import shsjxzh.compiler.IR.Value.Register;
import shsjxzh.compiler.IR.Value.Value;

public class IntCompare extends Instruction {
    public enum CompareOp{
        EQ, NE, GT, GE, LT, LE
    }

    private CompareOp op;
    private Register dest;
    private Value left;
    private Value right;

    public IntCompare(BasicBlock curBB, CompareOp op, Register dest, Value left, Value right) {
        super(curBB);
        this.op = op;
        this.dest = dest;
        this.left = left;
        this.right = right;
    }

    @Override
    public void accept(IRVisitor visitor) {
        visitor.visit(this);
    }
}
