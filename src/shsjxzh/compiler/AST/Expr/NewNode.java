package shsjxzh.compiler.AST.Expr;

import shsjxzh.compiler.AST.ASTVisitor;
import shsjxzh.compiler.AST.tool.Position;
import shsjxzh.compiler.Type.Type;

import java.util.List;

public class NewNode extends ExprNode{
    Type type;
    private List<ExprNode> exprDim;
    //private int nonExprDim;


    public NewNode(Position pos, Type type, List<ExprNode> exprDim) {
        super(pos);
        this.type = type;
        this.exprDim = exprDim;
    }

    public Type getType() {
        return type;
    }

    public List<ExprNode> getExprDim() {
        return exprDim;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
