package shsjxzh.compiler.AST.Decl;

import shsjxzh.compiler.AST.ASTVisitor;
import shsjxzh.compiler.AST.tool.Position;

import java.util.List;

public class ClassDeclNode extends DeclNode {
    private String className;
    private List<VarDeclNode> classMember;
    private List<FuncDeclNode> classMethod;
    private FuncDeclNode constructMethod;

    public ClassDeclNode(Position pos, String className, List<VarDeclNode> classMember, List<FuncDeclNode> classMethod, FuncDeclNode constructMethod) {
        super(pos);
        this.className = className;
        this.classMember = classMember;
        this.classMethod = classMethod;
        this.constructMethod = constructMethod;
    }

    @Override
    public String getName() {
        return className;
    }

    public List<VarDeclNode> getClassMember() {
        return classMember;
    }

    public List<FuncDeclNode> getClassMethod() {
        return classMethod;
    }

    public FuncDeclNode getConstructMethod() {
        return constructMethod;
    }

    @Override
    public void accept(ASTVisitor visitor) {
            visitor.visit(this);
    }
}
