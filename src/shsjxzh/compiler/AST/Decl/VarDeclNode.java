package shsjxzh.compiler.AST.Decl;

import shsjxzh.compiler.AST.ASTVisitor;
import shsjxzh.compiler.AST.Expr.ExprNode;
import shsjxzh.compiler.AST.tool.Position;
import shsjxzh.compiler.IR.Value.Register;
import shsjxzh.compiler.Type.Type;

public class VarDeclNode extends DeclNode {
    private Type varType;
    private String varName;
    private ExprNode expr;

    public Register varReg;

    public VarDeclNode(Position pos, Type varType, String varName, ExprNode expr) {
        super(pos);
        this.varType = varType;
        this.varName = varName;
        this.expr = expr;
    }

    public Type getVarType() {
        return varType;
    }

    @Override
    public String getName() {
        return varName;
    }

    public ExprNode getExpr() {
        return expr;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
