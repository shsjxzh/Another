package shsjxzh.compiler.AST;

import shsjxzh.compiler.AST.Decl.DeclNode;
import shsjxzh.compiler.AST.tool.Position;

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

    public List<DeclNode> getDeclnodes() {
        return declnodes;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
