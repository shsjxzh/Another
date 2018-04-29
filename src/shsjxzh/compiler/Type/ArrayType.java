package shsjxzh.compiler.Type;

//import shsjxzh.compiler.AST.tool.Position;

public class ArrayType extends Type{
    NonArrayType baseType;
    int dim;

    public ArrayType(NonArrayType baseType, int dim) {
        this.baseType = baseType;
        this.dim = dim;
    }

    @Override
    public String getDetail() {
        return "type kind: ArrayType, dim: " + String.valueOf(dim) + ", baseType: " + baseType.name;
    }
}
