package shsjxzh.compiler.Scope;

import shsjxzh.compiler.AST.Decl.DeclNode;
import shsjxzh.compiler.AST.tool.Position;
import shsjxzh.compiler.ErrorHandle.ErrorHandler;

import java.util.HashMap;
import java.util.Map;

public class GlobalScope extends Scope{

    public GlobalScope() {
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
        return null;
    }

    @Override
    public String getKind() {
        return "global";
    }

    @Override
    public DeclNode resolve(String name) {
        DeclNode s = entities.get(name);
        if (s != null) return s;
        return null;
    }

    @Override
    public DeclNode resolveThis(Position pos) {
        throw new ErrorHandler("Error using of \"This\"", pos);
    }

    @Override
    public String getName() {
        return "global";
    }
}
