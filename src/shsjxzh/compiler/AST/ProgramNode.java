package shsjxzh.compiler.AST;
//整个文件的根节点

import shsjxzh.compiler.AST.tool.Position;
import shsjxzh.compiler.Unuse.VarDeclStmtNode;

import java.util.List;

public class ProgramNode extends ASTNode{
    //private List<VarDeclStmtNode> varDecl;
    //private List<FuncDeclNode> funDecl;
    //private List<ClassDeclNode> classDecl;
    private List<DeclNode> declnodes;

    public ProgramNode(Position pos, List<DeclNode> declnodes) {
        super(pos);
        this.declnodes = declnodes;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
