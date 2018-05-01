package shsjxzh.compiler.AST.Expr;

import shsjxzh.compiler.AST.ASTNode;
import shsjxzh.compiler.AST.ASTVisitor;
import shsjxzh.compiler.AST.tool.Position;

public abstract class ExprNode extends ASTNode {
    //protected Type exprType;
    protected boolean isLvalue;

    public ExprNode(Position pos) {
        super(pos);
        this.isLvalue = false;
    }

    public boolean isLvalue()
    {
        return isLvalue;
    }

    @Override
    public abstract void accept(ASTVisitor visitor) ;
}
