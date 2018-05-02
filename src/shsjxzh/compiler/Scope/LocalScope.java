package shsjxzh.compiler.Scope;

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

    public LocalScope(String kind, Scope parent) {
        this.parent = parent;
        //注意，如果这是个不需要区分的类型，则会将类型直接置为null
        this.kind = kind;
        this.entities = new HashMap<>();
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

    //ToDo 完成这里的类联系
    @Override
    public DeclNode resolveThis(Position pos) {
        Scope tmp = parent;
        while(tmp != null && !tmp.getKind().equals("global")){
            if (tmp.getKind().equals("class")){

            }
            tmp = tmp.getParentScope();
        }
        throw new ErrorHandler("Error using of \"This\"", pos);
    }

    @Override
    public String getName() {
        return name;
    }
}
