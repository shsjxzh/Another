package shsjxzh.compiler.AST.Stmt;

import shsjxzh.compiler.AST.ASTVisitor;
import shsjxzh.compiler.AST.tool.Position;

public class BreakStmtNode extends StmtNode {
    public BreakStmtNode(Position pos) {
        super(pos);
    }

    //父类继承了accept方法之后，子类还需要写这个嘛？
    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
