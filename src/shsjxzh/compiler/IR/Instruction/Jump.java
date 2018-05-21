package shsjxzh.compiler.IR.Instruction;

import shsjxzh.compiler.IR.BasicBlock;
import shsjxzh.compiler.IR.IRVisitor;

public class Jump extends Instruction{
    private BasicBlock nextBB;

    public Jump(BasicBlock curBB, BasicBlock nextBB) {
        super(curBB);
        this.nextBB = nextBB;
    }

    @Override
    public void accept(IRVisitor visitor) {
        visitor.visit(this);
    }
}
