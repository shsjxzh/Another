package shsjxzh.compiler.AST.Decl;

import shsjxzh.compiler.AST.ASTVisitor;
import shsjxzh.compiler.AST.Stmt.BlockNode;
import shsjxzh.compiler.AST.TypeNode;
import shsjxzh.compiler.AST.tool.Position;

import java.util.List;

public class FuncDeclNode extends DeclNode {
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
