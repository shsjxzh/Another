package shsjxzh.compiler.Type;

import shsjxzh.compiler.AST.Decl.ClassDeclNode;
import shsjxzh.compiler.AST.tool.Position;

public class Type {
    String typeName;
    int dim;

    public Type(String typeName, int dim) {
        this.typeName = typeName;
        this.dim = dim;
    }

    public String getTypeName() {
        return typeName;
    }

    public int getDim() {
        return dim;
    }

    public void setDim(int dim) {
        this.dim = dim;
    }
    //专门用于类型消解
    ClassDeclNode typeDefinition;

    public ClassDeclNode getTypeDefinition() {
        return typeDefinition;
    }

    public void setTypeDefinition(ClassDeclNode typeDefinition) {
        this.typeDefinition = typeDefinition;
    }

    //用于类型检查

    @Override
    public String toString() {
        return ("type name: " + typeName + " dim: " + dim);
    }
}
