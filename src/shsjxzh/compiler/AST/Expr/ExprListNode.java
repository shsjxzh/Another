package shsjxzh.compiler.AST.Expr;

import shsjxzh.compiler.AST.ASTVisitor;
import shsjxzh.compiler.AST.tool.Position;

import java.util.List;

public class ExprListNode extends ExprNode {
    private List<ExprNode> exprList;

    public ExprListNode(Position pos, List<ExprNode> exprList) {
        super(pos);
        this.exprList = exprList;
    }

    public void addExpr(ExprNode node)
    {
        exprList.add(node);
    }

    @Override
    public void accept(ASTVisitor visitor) {
        super.accept(visitor);
    }
}
