package shsjxzh.compiler.Type;

//import shsjxzh.compiler.AST.tool.Position;

public class NonArrayType extends Type{
    public String name;

    public NonArrayType(String name) {
        this.name = name;
    }

    @Override
    public String getDetail() {
        return "type kind: NonArrayType, type name: " + name;
    }
}
