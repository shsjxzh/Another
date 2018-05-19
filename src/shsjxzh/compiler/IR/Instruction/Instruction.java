package shsjxzh.compiler.IR.Instruction;

import shsjxzh.compiler.IR.BasicBlock;

public abstract class Instruction {
    protected BasicBlock curBB;
    public Instruction prev = null;
    public Instruction next = null;

    public Instruction(BasicBlock curBB) {
        this.curBB = curBB;
    }

    public void setPrev(Instruction ins){
        this.prev = ins;
        ins.next = this;
    }

    public void setNext(Instruction ins){
        this.next = ins;
        ins.prev = this;
    }

}
