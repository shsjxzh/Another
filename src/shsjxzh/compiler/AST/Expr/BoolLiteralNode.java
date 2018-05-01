package shsjxzh.compiler.AST.Expr;

import shsjxzh.compiler.AST.ASTVisitor;
import shsjxzh.compiler.AST.tool.Position;
//import shsjxzh.compiler.Unuse.Type.BuildInType;

public class BoolLiteralNode extends LiteralNode{
    private boolean value;

    public BoolLiteralNode(Position pos, boolean value) {
        super(pos);
        this.value = value;
        //this.exprType = new BuildInType("bool",1);
    }

    public boolean getValue() {
        return value;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
