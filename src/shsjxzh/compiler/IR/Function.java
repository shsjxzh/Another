package shsjxzh.compiler.IR;

import shsjxzh.compiler.IR.Value.VirtualRegister;

import java.util.ArrayList;
import java.util.List;

public class Function {
    public List<VirtualRegister> argvRegList = new ArrayList<>();
    private String name;
    public BasicBlock startBB;
    public BasicBlock exitBB;

    //determine the size of the return value
    private int retSize;

    public Function(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getRetSize() {
        return retSize;
    }

    public void accept(IRVisitor visitor){
        visitor.visit(this);
    }
}
