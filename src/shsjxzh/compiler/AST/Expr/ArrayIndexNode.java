package shsjxzh.compiler.AST.Expr;

import shsjxzh.compiler.AST.ASTVisitor;
import shsjxzh.compiler.AST.tool.Position;

public class ArrayIndexNode extends ExprNode {
    ExprNode array;
    ExprNode index;

    public ArrayIndexNode(Position pos, ExprNode array, ExprNode index) {
        super(pos);
        this.array = array;
        this.index = index;
        this.isLvalue = true;
    }
    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }

    public ExprNode getArray() {
        return array;
    }

    public ExprNode getIndex() {
        return index;
    }
}
