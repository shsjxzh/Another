package shsjxzh.compiler.IR;

import shsjxzh.compiler.AST.Decl.FuncDeclNode;

public class Function{
    private String name;
    private BasicBlock startBB;
    private BasicBlock exitBB;
    private int returnSize;

    public Function(FuncDeclNode node){
        name = node.getName();
        //returnSize = node.
    }
}
