package shsjxzh.compiler.IR;

import shsjxzh.compiler.AST.Decl.FuncDeclNode;

public class Function{
    private String name;
    private BasicBlock startBB;
    private BasicBlock exitBB;
    private int returnSize;

    public Function(String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStartBB(BasicBlock startBB) {
        this.startBB = startBB;
    }

    public void setExitBB(BasicBlock exitBB) {
        this.exitBB = exitBB;
    }

    public void setReturnSize(int returnSize) {
        this.returnSize = returnSize;
    }

    public String getName() {
        return name;
    }

    public BasicBlock getStartBB() {
        return startBB;
    }

    public BasicBlock getExitBB() {
        return exitBB;
    }

    public int getReturnSize() {
        return returnSize;
    }
}
