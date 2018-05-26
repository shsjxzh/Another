package shsjxzh.compiler.IR.Instruction;

import shsjxzh.compiler.IR.BasicBlock;
import shsjxzh.compiler.IR.IR;
import shsjxzh.compiler.IR.IRVisitor;

public abstract class Instruction extends IR {
    protected BasicBlock belongBB;
    protected Instruction prev = null;
    protected Instruction next = null;

    public Instruction(BasicBlock belongBB) {
        this.belongBB = belongBB;
    }

    public void LinkNext(Instruction ins){
        this.next = ins;
        ins.prev = this;
    }

    public void LinkPrev(Instruction ins){
        this.prev = ins;
        ins.next = this;
    }

    public BasicBlock getBelongBB() {
        return belongBB;
    }

    public Instruction Next(){
        return this.next;
    }
}
