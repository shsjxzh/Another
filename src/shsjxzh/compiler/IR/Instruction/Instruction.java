package shsjxzh.compiler.IR.Instruction;

import shsjxzh.compiler.IR.BasicBlock;
import shsjxzh.compiler.IR.IRVisitor;

public abstract class Instruction {
    protected BasicBlock curBB;
    protected Instruction prev;
    protected Instruction next;

    public abstract void accept(IRVisitor visitor);

    public Instruction(BasicBlock curBB) {
        this.curBB = curBB;
    }

    public void LinkNext(Instruction ins){
        this.next = ins;
        ins.prev = this;
    }

    public void LinkPrev(Instruction ins){
        this.prev = ins;
        ins.next = this;
    }
}
