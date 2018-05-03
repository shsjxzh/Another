package shsjxzh.compiler.AST.Expr;

import shsjxzh.compiler.AST.ASTNode;
import shsjxzh.compiler.AST.ASTVisitor;
import shsjxzh.compiler.AST.tool.Position;
import shsjxzh.compiler.Type.Type;

public abstract class ExprNode extends ASTNode {
    protected boolean isLvalue;
    public Type exprType;

    public ExprNode(Position pos) {
        super(pos);
        this.isLvalue = false;
    }

    public boolean isLvalue()
    {
        return isLvalue;
    }

    public Type getExprType() {
        return exprType;
    }

    public abstract void initExprType();

    @Override
    public abstract void accept(ASTVisitor visitor) ;
}
