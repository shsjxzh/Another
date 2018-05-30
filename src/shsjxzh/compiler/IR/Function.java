package shsjxzh.compiler.IR;

import shsjxzh.compiler.AST.Decl.FuncDeclNode;
import shsjxzh.compiler.IR.Value.VirtualRegister;

import java.util.ArrayList;
import java.util.List;

public class Function extends IR{
    private String name;
    private BasicBlock startBB;
    public List<VirtualRegister> funcParams = new ArrayList<>();
    //private BasicBlock exitBB;
    private int returnSize;
    private int returnNum;
    private boolean buildIn;

    public Function(String name){
        this.name = name;
        buildIn = true;
    }

    public Function(String name, String startBlockName,int returnNum, int returnSize) {
        this.name = name;
        startBB = new BasicBlock(startBlockName,this);
        this.returnNum = returnNum;
        this.returnSize = returnSize;
        buildIn = false;
    }

    public int getReturnStmtNum() {
        return returnNum;
    }

    public boolean isBuildIn() {
        return buildIn;
    }

    /*
    public void setExitBB(BasicBlock exitBB) {
        this.exitBB = exitBB;
    }*/

    //public void setReturnSize(int returnSize) {
      //  this.returnSize = returnSize;
    //}

    public String getName() {
        return name;
    }

    public BasicBlock getStartBB() {
        return startBB;
    }

    /*public BasicBlock getExitBB() {
        return exitBB;
    }*/

    public int getReturnSize() {
        return returnSize;
    }

    @Override
    public void accept(IRVisitor visitor) {
        visitor.visit(this);
    }
}
