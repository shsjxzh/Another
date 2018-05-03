package shsjxzh.compiler.AST.Expr;

import shsjxzh.compiler.AST.ASTVisitor;
import shsjxzh.compiler.AST.tool.Position;
import shsjxzh.compiler.ErrorHandle.ErrorHandler;
import shsjxzh.compiler.Type.Type;

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

    @Override
    public void initExprType() {
        Type arrayType = array.getExprType();
        exprType = new Type(arrayType.getTypeName(),arrayType.getDim() - 1);
        if (exprType.getDim() < 0){
            throw new ErrorHandler("Error index using ", this.pos);
        }
    }
}
