package shsjxzh.compiler.AST.Decl;

import shsjxzh.compiler.AST.ASTVisitor;
import shsjxzh.compiler.AST.Decl.DeclNode;
import shsjxzh.compiler.AST.Expr.ExprNode;

import shsjxzh.compiler.AST.TypeNode;
import shsjxzh.compiler.AST.tool.Position;

public class VarDeclNode extends DeclNode {
    private TypeNode varType;
    private String varName;
    private ExprNode expr;

    public VarDeclNode(Position pos, TypeNode varType, String varName, ExprNode expr) {
        super(pos);
        this.varType = varType;
        this.varName = varName;
        this.expr = expr;
    }

    public TypeNode getVarType() {
        return varType;
    }

    public String getVarName() {
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
