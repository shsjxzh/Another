package shsjxzh.compiler.AST.Expr;

import shsjxzh.compiler.AST.ASTVisitor;
import shsjxzh.compiler.AST.Decl.ClassDeclNode;
import shsjxzh.compiler.AST.tool.Position;
import shsjxzh.compiler.Type.Type;

public class ThisNode extends ExprNode{
    public ThisNode(Position pos) {
        super(pos);
    }

    //For type reference
    private ClassDeclNode thisDefinition;

    public ClassDeclNode getThisDefinition() {
        return thisDefinition;
    }

    public void setThisDefinition(ClassDeclNode thisDefinition) {
        this.thisDefinition = thisDefinition;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void initExprType() {
        exprType = new Type(thisDefinition.getName(),0);
    }
}
