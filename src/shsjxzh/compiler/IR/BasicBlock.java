package shsjxzh.compiler.IR;


import shsjxzh.compiler.IR.Instruction.Instruction;

public class BasicBlock {
    private Instruction head = null;
    private Instruction last = null;
    //private boolean ended = false;
    private String hintName;

    public void accept(IRVisitor visitor){
        visitor.visit(this);
    }
}
