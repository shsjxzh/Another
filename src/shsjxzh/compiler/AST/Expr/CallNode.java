package shsjxzh.compiler.AST.Expr;

import shsjxzh.compiler.AST.ASTVisitor;
import shsjxzh.compiler.AST.tool.Position;

import java.util.List;

public class CallNode extends ExprNode{
    private String funcName;
    private List<ExprNode> funcParams;

    public CallNode(Position pos, String funcName, List<ExprNode> funcParams) {
        super(pos);
        this.funcName = funcName;
        this.funcParams = funcParams;
    }

    public String getFuncName() {
        return funcName;
    }

    public List<ExprNode> getFuncParams() {
        return funcParams;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
