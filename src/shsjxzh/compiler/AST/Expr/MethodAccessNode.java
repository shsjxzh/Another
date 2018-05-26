package shsjxzh.compiler.AST.Expr;

import shsjxzh.compiler.AST.ASTVisitor;
import shsjxzh.compiler.AST.Decl.FuncDeclNode;
import shsjxzh.compiler.AST.tool.Position;
import shsjxzh.compiler.Type.Type;

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
        //this.hasSideEffect = true;
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

    private FuncDeclNode funcDefinition;

    public void setFuncDefinition(FuncDeclNode funcDefinition) {
        this.funcDefinition = funcDefinition;
    }

    public FuncDeclNode getFuncDefinition() {
        return funcDefinition;
    }

    @Override
    public void initExprType() {
        if (methodName.equals("size")){
            //size will always return the first dim
            exprType = new Type("int",0);
        }
        else exprType = funcDefinition.getFuncReturnType();
    }
}
