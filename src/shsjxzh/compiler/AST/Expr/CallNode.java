package shsjxzh.compiler.AST.Expr;

import shsjxzh.compiler.AST.ASTVisitor;
import shsjxzh.compiler.AST.Decl.FuncDeclNode;
import shsjxzh.compiler.AST.tool.Position;

import java.util.List;

//find function definition
public class CallNode extends ExprNode{
    private String funcName;
    private List<ExprNode> funcParams;

    public CallNode(Position pos, String funcName, List<ExprNode> funcParams) {
        super(pos);
        this.funcName = funcName;
        this.funcParams = funcParams;
        //this.hasSideEffect = true;
    }

    public String getFuncName() {
        return funcName;
    }

    public List<ExprNode> getFuncParams() {
        return funcParams;
    }

    private FuncDeclNode funcDefinition;

    public void setFuncDefinition(FuncDeclNode funcDefinition) {
        this.funcDefinition = funcDefinition;
    }

    public FuncDeclNode getFuncDefinition() {
        return funcDefinition;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void initExprType() {
        exprType = funcDefinition.getFuncReturnType();
    }
}
