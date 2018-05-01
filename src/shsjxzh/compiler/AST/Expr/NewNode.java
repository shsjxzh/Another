package shsjxzh.compiler.AST.Expr;

import shsjxzh.compiler.AST.ASTVisitor;
import shsjxzh.compiler.AST.TypeNode;
import shsjxzh.compiler.AST.tool.Position;

import java.util.List;

public class NewNode extends ExprNode{
    TypeNode type;
    private List<ExprNode> exprDim;
    private int nonExprDim;

    public NewNode(Position pos, TypeNode type, List<ExprNode> exprDim, int nonExprDim) {
        super(pos);
        this.type = type;
        this.exprDim = exprDim;
        this.nonExprDim = nonExprDim;
    }

    public TypeNode getType() {
        return type;
    }

    public List<ExprNode> getExprDim() {
        return exprDim;
    }

    public int getNonExprDim() {
        return nonExprDim;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
