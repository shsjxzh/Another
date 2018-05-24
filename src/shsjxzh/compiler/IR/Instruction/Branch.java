package shsjxzh.compiler.IR.Instruction;

import shsjxzh.compiler.IR.BasicBlock;
import shsjxzh.compiler.IR.IRVisitor;
import shsjxzh.compiler.IR.Value.Value;

public class Branch extends Instruction {
    private Value cond;
    private BasicBlock then;
    private BasicBlock otherwise;

    public Branch(BasicBlock curBB, Value cond, BasicBlock then, BasicBlock otherwise) {
        super(curBB);
        this.cond = cond;
        this.then = then;
        this.otherwise = otherwise;
    }

    public Value getCond() {
        return cond;
    }

    public BasicBlock getThen() {
        return then;
    }

    public BasicBlock getOtherwise() {
        return otherwise;
    }

    @Override
    public void accept(IRVisitor visitor) {
        visitor.visit(this);
    }
}
