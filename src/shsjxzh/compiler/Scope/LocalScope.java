package shsjxzh.compiler.Scope;

import shsjxzh.compiler.AST.Decl.DeclNode;
import shsjxzh.compiler.ErrorHandle.ErrorHandler;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class LocalScope extends Scope{
    public String kind;
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
}
