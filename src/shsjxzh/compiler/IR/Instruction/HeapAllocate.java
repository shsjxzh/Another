package shsjxzh.compiler.IR.Instruction;

import shsjxzh.compiler.IR.BasicBlock;
import shsjxzh.compiler.IR.IRVisitor;
import shsjxzh.compiler.IR.Value.Register;
import shsjxzh.compiler.IR.Value.Value;

public class HeapAllocate extends Instruction {
    //store the address
    private Register dest;
    private Value size;

    public HeapAllocate(BasicBlock curBB, Register dest, Value size) {
        super(curBB);
        this.dest = dest;
        this.size = size;
    }

    @Override
    public void accept(IRVisitor visitor) {
        visitor.visit(this);
    }
}
