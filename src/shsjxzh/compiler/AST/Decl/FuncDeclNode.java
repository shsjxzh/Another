package shsjxzh.compiler.AST.Decl;

import shsjxzh.compiler.AST.ASTVisitor;
import shsjxzh.compiler.AST.Stmt.BlockNode;
import shsjxzh.compiler.AST.Stmt.ReturnStmtNode;
import shsjxzh.compiler.AST.tool.Position;
import shsjxzh.compiler.Type.Type;

import java.util.List;

public class FuncDeclNode extends DeclNode {
    private Type funcReturnType;
    private BlockNode funcBlock;
    private String funcName;
    private List<VarDeclNode> funcParams;

    public FuncDeclNode(Position pos, Type funcReturnType, BlockNode funcBlock, String funcName, List<VarDeclNode> funcParams) {
        super(pos);
        this.funcReturnType = funcReturnType;
        this.funcBlock = funcBlock;
        this.funcName = funcName;
        this.funcParams = funcParams;
        this.buildIn = false;
    }

    public Type getFuncReturnType() {
        return funcReturnType;
    }

    public BlockNode getFuncBlock() {
        return funcBlock;
    }

    @Override
    public String getName() {
        return funcName;
    }

    public List<VarDeclNode> getFuncParams() {
        return funcParams;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }

    //return
    private boolean buildIn;
    public void setBuildIn(boolean buildIn) {
        this.buildIn = buildIn;
    }


    public FuncDeclNode(Position pos, Type funcReturnType, BlockNode funcBlock, String funcName, List<VarDeclNode> funcParams, boolean buildIn) {
        super(pos);
        this.funcReturnType = funcReturnType;
        this.funcBlock = funcBlock;
        this.funcName = funcName;
        this.funcParams = funcParams;
        this.buildIn = buildIn;
    }
}

