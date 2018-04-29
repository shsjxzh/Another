package shsjxzh.compiler.AST.Expr;

import shsjxzh.compiler.AST.ASTVisitor;
import shsjxzh.compiler.AST.tool.Position;

import java.util.List;

public class NewNode extends ExprNode{
    private List<ExprNode> dim;

    public NewNode(Position pos, List<ExprNode> dim) {
        super(pos);
        this.dim = dim;
    }

    public void addDim(ExprNode node){
        dim.add(node);
    }

    @Override
    public void accept(ASTVisitor visitor) {
        super.accept(visitor);
    }
}
