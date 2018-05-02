package shsjxzh.compiler.AST.Expr;

import shsjxzh.compiler.AST.ASTVisitor;
import shsjxzh.compiler.AST.Decl.DeclNode;
import shsjxzh.compiler.AST.Decl.VarDeclNode;
import shsjxzh.compiler.AST.tool.Position;

//find value definition
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

    VarDeclNode valueDefinition;

    public void setValueDefinition(VarDeclNode valueDefinition) {
        this.valueDefinition = valueDefinition;
    }
}
