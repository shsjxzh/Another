package shsjxzh.compiler.AST.Expr;

import shsjxzh.compiler.AST.ASTVisitor;
import shsjxzh.compiler.AST.Decl.ClassDeclNode;
import shsjxzh.compiler.AST.Decl.VarDeclNode;
import shsjxzh.compiler.AST.tool.Position;
import shsjxzh.compiler.Type.Type;

public class ThisNode extends ExprNode{
    public ThisNode(Position pos) {
        super(pos);
    }

    //For type reference
    /*private ClassDeclNode thisDefinition;

    public ClassDeclNode getThisDefinition() {
        return thisDefinition;
    }

    public void setThisDefinition(ClassDeclNode thisDefinition) {
        this.thisDefinition = thisDefinition;
    }*/
    VarDeclNode valueDefinition;

    public void setValueDefinition(VarDeclNode valueDefinition) {
        this.valueDefinition = valueDefinition;
    }

    public VarDeclNode getValueDefinition() {
        return valueDefinition;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void initExprType() {
        exprType = valueDefinition.getVarType();
    }
}
