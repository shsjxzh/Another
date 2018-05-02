package shsjxzh.compiler.Scope;

import shsjxzh.compiler.AST.ASTVisitor;
import shsjxzh.compiler.AST.Decl.DeclNode;
import shsjxzh.compiler.AST.tool.Position;
import shsjxzh.compiler.ErrorHandle.ErrorHandler;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class LocalScope extends Scope{
    public String kind;
    public String name;
    public Scope parent;

    public LocalScope(String kind, String name, Scope parent) {
        this.kind = kind;
        this.name = name;
        this.parent = parent;
        entities = new HashMap<>();
    }

    @Override
    public void define(DeclNode entity) {
        if (entities.get(entity.getName()) != null){
            throw new ErrorHandler("Duplicate Entities", entity.getPos());
        }
        entities.put(entity.getName(), entity);
    }

    @Override
    public Scope getParentScope() {
        return parent;
    }

    @Override
    public String getKind() {
        return kind;
    }

    @Override
    public DeclNode resolve(String name) {
        DeclNode s = entities.get(name);
        if (s != null) return s;
        if (parent != null) return parent.resolve(name);
        return null;
    }

    //ToDo "this"
    @Override
    public DeclNode resolveThis() {
        Scope tmp = this;
        boolean goThroughFunc = false;
        while(tmp != null && !tmp.getKind().equals("global")){
            if (tmp.getKind().equals("class")){
                if (goThroughFunc) {
                    return tmp.entities.get(tmp.getName());
                }
                else return null;
            }
            if (tmp.getKind().equals("function")){
                goThroughFunc = true;
            }
            tmp = tmp.getParentScope();
        }
        return null;
    }

    @Override
    public String getName() {
        return name;
    }
}
