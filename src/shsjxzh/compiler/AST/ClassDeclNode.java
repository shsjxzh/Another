package shsjxzh.compiler.AST;

//import shsjxzh.compiler.AST.Object.ClassObject;
import shsjxzh.compiler.AST.tool.Position;

import java.util.List;

public class ClassDeclNode extends DeclNode{
    private String className;
    //如果发现有问题再实现专门的classMember和classMethod节点
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
    public void accept(ASTVisitor visitor) {
            visitor.visit(this);
    }
}
