package shsjxzh.compiler.AST.Expr;

import shsjxzh.compiler.AST.ASTVisitor;
import shsjxzh.compiler.AST.Decl.ClassDeclNode;
import shsjxzh.compiler.AST.tool.Position;

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
}
