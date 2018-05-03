package shsjxzh.compiler.AST.Expr;

import shsjxzh.compiler.AST.ASTVisitor;
import shsjxzh.compiler.AST.tool.Position;
import shsjxzh.compiler.Type.Type;

import java.util.List;

public class NewNode extends ExprNode{
    //Type exprtype;
    private List<ExprNode> exprDim;
    //private int nonExprDim;


    public NewNode(Position pos, List<ExprNode> exprDim, Type exprType) {
        super(pos);
        this.exprDim = exprDim;
        this.exprType = exprType;
    }

    public List<ExprNode> getExprDim() {
        return exprDim;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void initExprType() {
        //do nothing
    }
}
