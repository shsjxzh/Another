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

    //type reference
    ClassDeclNode typeDefinition;

    public ClassDeclNode getTypeDefinition() {
        return typeDefinition;
    }

    public void setTypeDefinition(ClassDeclNode typeDefinition) {
        this.typeDefinition = typeDefinition;
    }

    //type check
    //not override
    public boolean equals(Type obj) {
        if (obj != null){
            if (obj.getTypeName().equals(typeName) && obj.getDim() == dim){
                return true;
            }
        }
        return false;
    }

    public boolean isArray(){
        return dim > 0;
    }

    public boolean isBuildInType(){
        return typeName.equals("bool") || typeName.equals("string") || typeName.equals("int");
    }

    public boolean isBool(){
        return typeName.equals("bool") && dim == 0;
    }

    public boolean isString(){
        return typeName.equals("string") && dim == 0;
    }

    public boolean isInt(){
        return typeName.equals("int") && dim == 0;
    }

    public boolean isNull() { return typeName.equals("null") && dim == 0; }

    public int getRegisterSize(){
        int wise = 8;

        if (isNull()) return 0;
        else {
            //now it is pointer or int, bool
            return wise;
        }
    }

    public int getTypeMemSize(){
        return typeDefinition.getAllocSize();
    }

    @Override
    public String toString() {
        return ("type name: " + typeName + " dim: " + dim);
    }
}
