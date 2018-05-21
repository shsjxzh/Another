package shsjxzh.compiler.IR.Instruction;

import shsjxzh.compiler.IR.BasicBlock;
import shsjxzh.compiler.IR.IRVisitor;
import shsjxzh.compiler.IR.Value.Register;
import shsjxzh.compiler.IR.Value.Value;

public class Move extends Instruction {
    private Register dest;
    //register or immediate
    private Value source;

    public Move(BasicBlock curBB, Register dest, Value source) {
        super(curBB);
        this.dest = dest;
        this.source = source;
    }

    @Override
    public void accept(IRVisitor visitor) {
        visitor.visit(this);
    }
}