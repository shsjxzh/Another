package shsjxzh.compiler.AST;

import shsjxzh.compiler.AST.ASTNode;
import shsjxzh.compiler.AST.ASTVisitor;
import shsjxzh.compiler.AST.tool.Position;
import shsjxzh.compiler.Type.*;
public class TypeNode extends ASTNode {
    private Type type;

    public TypeNode(Position pos, Type type) {
        super(pos);
        this.type = type;
    }

    public TypeNode(Position pos) {
        super(pos);
        //用来应对void类型
        this.type = null;
    }

    public void setType(String name, int dim){
        if (dim == 0){
            type =  new NonArrayType(name);
        }
        else {
            NonArrayType baseType = new NonArrayType(name);
            type = new ArrayType(baseType, dim);
        }
    }

    public Type getType() {
        return type;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
