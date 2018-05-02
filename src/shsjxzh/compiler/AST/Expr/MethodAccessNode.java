package shsjxzh.compiler.AST.Expr;

import shsjxzh.compiler.AST.ASTVisitor;
import shsjxzh.compiler.AST.tool.Position;

import java.util.List;

public class MethodAccessNode extends ExprNode {
    ExprNode object;
    String methodName;
    List<ExprNode> methodParams;

    public MethodAccessNode(Position pos, ExprNode object, String methodName, List<ExprNode> methodParams) {
        super(pos);
        this.object = object;
        this.methodName = methodName;
        this.methodParams = methodParams;
    }

    public ExprNode getObject() {
        return object;
    }

    public String getMethodName() {
        return methodName;
    }

    public List<ExprNode> getMethodParams() {
        return methodParams;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
