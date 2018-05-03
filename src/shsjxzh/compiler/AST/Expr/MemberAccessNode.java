package shsjxzh.compiler.AST.Expr;

import shsjxzh.compiler.AST.ASTVisitor;
import shsjxzh.compiler.AST.Decl.FuncDeclNode;
import shsjxzh.compiler.AST.Decl.VarDeclNode;
import shsjxzh.compiler.AST.tool.Position;

public class MemberAccessNode extends ExprNode{
    ExprNode object;
    String memberRef;

    public MemberAccessNode(Position pos, ExprNode object, String memberRef) {
        super(pos);
        this.object = object;
        this.memberRef = memberRef;
        this.isLvalue = true;
    }

    public ExprNode getObject() {
        return object;
    }

    public String getMemberRef() {
        return memberRef;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }

    VarDeclNode valueDefinition;

    public void setValueDefinition(VarDeclNode valueDefinition) {
        this.valueDefinition = valueDefinition;
    }

    @Override
    public void initExprType() {
        exprType = valueDefinition.getVarType();
    }
}
