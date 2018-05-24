package shsjxzh.compiler.AST;

import shsjxzh.compiler.AST.Decl.DeclNode;
import shsjxzh.compiler.AST.Decl.FuncDeclNode;
import shsjxzh.compiler.AST.tool.Position;

import java.util.List;

public class ProgramNode extends ASTNode{
    //private List<VarDeclStmtNode> varDecl;
    //private List<FuncDeclNode> funDecl;
    //private List<ClassDeclNode> classDecl;
    private List<DeclNode> declnodes;
    private DeclNode mainDecl;

    public ProgramNode(Position pos, List<DeclNode> declnodes, DeclNode mainDecl) {
        super(pos);
        this.declnodes = declnodes;
        this.mainDecl = mainDecl;
    }

    public List<DeclNode> getDeclnodes() {
        return declnodes;
    }

    public DeclNode getMainDecl() {
        return mainDecl;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
