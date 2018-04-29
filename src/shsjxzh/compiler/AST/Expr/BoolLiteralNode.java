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

    @Override
    public void accept(ASTVisitor visitor) {
        super.accept(visitor);
    }
}
