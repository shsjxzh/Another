package shsjxzh.compiler.AST.Expr;

import shsjxzh.compiler.AST.ASTVisitor;
import shsjxzh.compiler.AST.Decl.DeclNode;
import shsjxzh.compiler.AST.tool.Position;

public class VariableNode extends ExprNode{
    String name;

    public VariableNode(Position pos, String name) {
        super(pos);
        this.name = name;
        this.isLvalue = true;
    }

    public String getName() {
        return name;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }

    DeclNode entity;

    public void setEntity(DeclNode entity) {
        this.entity = entity;
    }
}
