package shsjxzh.compiler.IR.Instruction;

import shsjxzh.compiler.IR.BasicBlock;
import shsjxzh.compiler.IR.IR;
import shsjxzh.compiler.IR.IRVisitor;

public abstract class Instruction extends IR {
    protected BasicBlock curBB;
    protected Instruction prev = null;
    protected Instruction next = null;

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

    public Instruction Next(){
        return this.next;
    }
}
