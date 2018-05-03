package shsjxzh.compiler.AST.Expr;

import shsjxzh.compiler.AST.ASTVisitor;
import shsjxzh.compiler.AST.tool.Position;
import shsjxzh.compiler.Type.Type;
//import shsjxzh.compiler.Unuse.Type.BuildInType;

public class IntLiteralNode extends LiteralNode {
    private int value;

    public IntLiteralNode(Position pos, int value) {
        super(pos);
        this.value = value;
        //this.exprType = new BuildInType("int", 1);
    }

    public int getValue() {
        return value;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void initExprType() {
        exprType = new Type("int",0);
    }
}
