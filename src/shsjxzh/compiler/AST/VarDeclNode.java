package shsjxzh.compiler.AST;

import shsjxzh.compiler.AST.Expr.ExprNode;
//import shsjxzh.compiler.AST.Object.VarObject;
import shsjxzh.compiler.AST.tool.Position;
//import shsjxzh.compiler.Type.Type;
//import shsjxzh.compiler.Unuse.Type.Type;

public class VarDeclNode extends DeclNode{
    private TypeNode varType;
    private String varName;
    private ExprNode expr;

    public VarDeclNode(Position pos, TypeNode varType, String varName, ExprNode expr) {
        super(pos);
        this.varType = varType;
        this.varName = varName;
        this.expr = expr;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
