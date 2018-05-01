package shsjxzh.compiler.AST.Expr;

import shsjxzh.compiler.AST.ASTVisitor;
import shsjxzh.compiler.AST.tool.Position;
//import shsjxzh.compiler.Unuse.Type.BuildInType;

public class StringLiteralNode extends LiteralNode{
    private String value;

    public StringLiteralNode(Position pos, String value) {
        super(pos);
        this.value = value;
        //this.exprType = new BuildInType("String", 1);
    }

    public String getValue() {
        return value;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
