package shsjxzh.compiler.IR.Instruction;

import shsjxzh.compiler.IR.BasicBlock;
import shsjxzh.compiler.IR.IRVisitor;
import shsjxzh.compiler.IR.Value.Value;

public class Return extends Instruction {
    private Value returnValue;

    public Return(BasicBlock curBB, Value returnValue) {
        super(curBB);
        this.returnValue = returnValue;
    }

    public Value getReturnValue() {
        return returnValue;
    }

    @Override
    public void accept(IRVisitor visitor) {
        visitor.visit(this);
    }
}
