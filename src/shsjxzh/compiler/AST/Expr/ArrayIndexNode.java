package shsjxzh.compiler.AST.Expr;

import shsjxzh.compiler.AST.ASTVisitor;
import shsjxzh.compiler.AST.tool.Position;

public class ArrayIndexNode extends ExprNode {
    String name;
    int index;

    public ArrayIndexNode(Position pos, String name, int index) {
        super(pos);
        this.name = name;
        this.index = index;
        this.isLvalue = true;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        super.accept(visitor);
    }
}
