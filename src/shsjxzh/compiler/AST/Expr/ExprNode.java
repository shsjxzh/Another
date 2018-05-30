package shsjxzh.compiler.AST.Expr;

import shsjxzh.compiler.AST.ASTNode;
import shsjxzh.compiler.AST.ASTVisitor;
import shsjxzh.compiler.AST.tool.Position;
import shsjxzh.compiler.IR.BasicBlock;
import shsjxzh.compiler.IR.Value.IntImme;
import shsjxzh.compiler.IR.Value.Register;
import shsjxzh.compiler.IR.Value.Value;
import shsjxzh.compiler.Type.Type;

public abstract class ExprNode extends ASTNode {
    protected boolean isLvalue;
    public Type exprType;

    public ExprNode(Position pos) {
        super(pos);
        this.isLvalue = false;
    }

    public boolean isLvalue()
    {
        return isLvalue;
    }

    public Type getExprType() {
        return exprType;
    }

    public abstract void initExprType();

    //For IR generater
    //some Exprnode refers to an register, address, etc
    public Value regOrImm;
    //logical node are linked with new basic blocks
    public BasicBlock ifTrue = null;
    public BasicBlock ifFalse = null;

    //for get address
    public Register Base = null;
    public Register Index = null;
    public IntImme displacement = null;
    public int scale = 0;

    @Override
    public abstract void accept(ASTVisitor visitor) ;
}
