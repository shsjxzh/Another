package shsjxzh.compiler.IR.Instruction;

import shsjxzh.compiler.IR.BasicBlock;
import shsjxzh.compiler.IR.IRVisitor;

public class Jump extends Instruction{
    private BasicBlock next;

    public Jump(BasicBlock curBB, BasicBlock next) {
        super(curBB);
        this.next = next;
    }

    public BasicBlock getNextBB() {
        return next;
    }

    @Override
    public void accept(IRVisitor visitor) {
        visitor.visit(this);
    }
}
