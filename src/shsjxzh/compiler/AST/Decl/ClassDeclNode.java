package shsjxzh.compiler.AST.Decl;

import shsjxzh.compiler.AST.ASTVisitor;
import shsjxzh.compiler.AST.tool.Position;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassDeclNode extends DeclNode {
    private String className;
    private List<VarDeclNode> classMember;
    private List<FuncDeclNode> classMethod;
    private FuncDeclNode constructMethod;
    private int allocSize;

    public Map<String, Integer> MemOffset = new HashMap<>();
    //private boolean isbuildIn = false;
    //private final int pointerSize = 8;

    public ClassDeclNode(Position pos, String className, List<VarDeclNode> classMember, List<FuncDeclNode> classMethod, FuncDeclNode constructMethod) {
        super(pos);
        this.className = className;
        this.classMember = classMember;
        this.classMethod = classMethod;
        this.constructMethod = constructMethod;

        //it is a little tricky, may need some change
        int wise = 8;
        this.allocSize = wise * classMember.size();
        int curOffset = 0;
        for (VarDeclNode member : classMember) {
            MemOffset.put(member.getName(), curOffset);
            curOffset += member.getVarType().getRegisterSize();
        }
    }

    public ClassDeclNode(Position pos, String className, List<VarDeclNode> classMember, List<FuncDeclNode> classMethod, FuncDeclNode constructMethod, boolean buildin) {
        super(pos);
        this.className = className;
        this.classMember = classMember;
        this.classMethod = classMethod;
        this.constructMethod = constructMethod;

        //it is a little tricky, may need some change
        int wise = 8;
        this.allocSize = wise;
        this.isBuildIn = buildin;
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

    public int getAllocSize() {
        return allocSize;
    }

    @Override
    public void accept(ASTVisitor visitor) {
            visitor.visit(this);
    }
}
