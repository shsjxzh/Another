package shsjxzh.compiler.IR;


import shsjxzh.compiler.IR.Instruction.Instruction;

public class BasicBlock {
    private Instruction head = null;
    private Instruction last = null;
    //private boolean ended = false;
    private String hintName;
    private BasicBlock left;
    private BasicBlock right;

    public BasicBlock(String hintName) {
        this.hintName = hintName;
    }

    public void setHead(Instruction head) {
        this.head = head;
    }

    public void setLast(Instruction last) {
        this.last = last;
    }

    public void accept(IRVisitor visitor){
        visitor.visit(this);
    }
}
