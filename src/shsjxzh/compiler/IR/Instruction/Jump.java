package shsjxzh.compiler.IR.Instruction;

import shsjxzh.compiler.IR.BasicBlock;
import shsjxzh.compiler.IR.IRVisitor;

public class Jump extends Instruction{
    private String nextLabel;

    public Jump(BasicBlock curBB, String nextLabel) {
        super(curBB);
        this.nextLabel = nextLabel;
    }

    /*public BasicBlock getNextBB() {
        return next;
    }*/

    public String getNextLabel() {
        return nextLabel;
    }

    @Override
    public void accept(IRVisitor visitor) {
        visitor.visit(this);
    }
}
