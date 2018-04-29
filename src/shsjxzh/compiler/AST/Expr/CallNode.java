package shsjxzh.compiler.AST.Expr;

import shsjxzh.compiler.AST.ASTVisitor;
import shsjxzh.compiler.AST.tool.Position;

public class CallNode extends ExprNode{
    private String funcName;
    private ExprListNode funcParams;

    public CallNode(Position pos, String funcName, ExprListNode funcParams) {
        super(pos);
        this.funcName = funcName;
        this.funcParams = funcParams;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        super.accept(visitor);
    }
}
