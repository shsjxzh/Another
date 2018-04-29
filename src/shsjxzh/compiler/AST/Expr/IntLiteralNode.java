package shsjxzh.compiler.AST.Expr;

import shsjxzh.compiler.AST.ASTVisitor;
import shsjxzh.compiler.AST.tool.Position;
//import shsjxzh.compiler.Unuse.Type.BuildInType;

public class IntLiteralNode extends LiteralNode {
    private int value;

    public IntLiteralNode(Position pos, int value) {
        super(pos);
        this.value = value;
        //this.exprType = new BuildInType("int", 1);
    }

    @Override
    public void accept(ASTVisitor visitor) {
        super.accept(visitor);
    }
}
