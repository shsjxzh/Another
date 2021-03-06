package shsjxzh.compiler.Scope;

import shsjxzh.compiler.AST.Decl.DeclNode;
import shsjxzh.compiler.AST.Stmt.ReturnStmtNode;
import shsjxzh.compiler.AST.tool.Position;
import shsjxzh.compiler.ErrorHandle.ErrorHandler;

import java.util.HashMap;

public class GlobalScope extends Scope{

    public GlobalScope() {
        entities = new HashMap<>();
        childScope = new HashMap<>();
    }

    @Override
    public void define(DeclNode entity) {
        if (entities.get(entity.getName()) != null){
            throw new ErrorHandler("Duplicate Entities \"" + entity.getName() + "\"", entity.getPos());
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
    public DeclNode resolveThis() {
       return null;
    }

    @Override
    public String getName() {
        return "global";
    }

    @Override
    public boolean resolveBreakContinue() {
        return false;
    }

    @Override
    public boolean resolveReturn(ReturnStmtNode node) {
        return false;
    }
}