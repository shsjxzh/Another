package shsjxzh.compiler.AST;

import shsjxzh.compiler.AST.tool.Position;
import shsjxzh.compiler.Type.Type;

import java.util.List;

public class FuncDeclNode extends DeclNode{
    private TypeNode funcReturnType;
    private BlockNode funcBlock;
    private String funcName;
    private List<VarDeclNode> funcParams;

    public FuncDeclNode(Position pos, TypeNode funcReturnType, BlockNode funcBlock, String funcName, List<VarDeclNode> funcParams) {
        super(pos);
        this.funcReturnType = funcReturnType;
        this.funcBlock = funcBlock;
        this.funcName = funcName;
        this.funcParams = funcParams;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
