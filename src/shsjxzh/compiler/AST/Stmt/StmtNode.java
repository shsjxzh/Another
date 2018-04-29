package shsjxzh.compiler.AST.Stmt;

import shsjxzh.compiler.AST.ASTNode;
import shsjxzh.compiler.AST.ASTVisitor;
import shsjxzh.compiler.AST.tool.Position;

public abstract class StmtNode extends ASTNode {
    public StmtNode(Position pos) {
        super(pos);
    }

    //@Override
    public abstract void accept(ASTVisitor visitor) ;
}
