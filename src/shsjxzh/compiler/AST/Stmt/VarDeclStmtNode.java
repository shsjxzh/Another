package shsjxzh.compiler.AST.Stmt;

import shsjxzh.compiler.AST.ASTVisitor;
import shsjxzh.compiler.AST.Decl.VarDeclNode;
import shsjxzh.compiler.AST.tool.Position;

public class VarDeclStmtNode extends StmtNode {
    private VarDeclNode varDecl;

    public VarDeclStmtNode(Position pos, VarDeclNode varDecl) {
        super(pos);
        this.varDecl = varDecl;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
