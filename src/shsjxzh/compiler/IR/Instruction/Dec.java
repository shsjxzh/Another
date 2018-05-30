package shsjxzh.compiler.IR.Instruction;

import shsjxzh.compiler.IR.BasicBlock;
import shsjxzh.compiler.IR.IRVisitor;
import shsjxzh.compiler.IR.Value.Register;

public class Dec extends Instruction{
    Register body;

    public Dec(BasicBlock belongBB, Register body) {
        super(belongBB);
        this.body = body;
    }

    public Register getBody() {
        return body;
    }

    @Override
    public void accept(IRVisitor visitor) {
        visitor.visit(this);
    }
}
