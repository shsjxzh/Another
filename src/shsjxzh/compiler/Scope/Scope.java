package shsjxzh.compiler.Scope;

import shsjxzh.compiler.AST.Decl.DeclNode;
import shsjxzh.compiler.AST.Stmt.ReturnStmtNode;
import shsjxzh.compiler.AST.tool.Position;

import java.util.List;
import java.util.Map;

abstract public class Scope {
    //protected List<LocalScope> children;
    public Map<String, DeclNode> entities;
    abstract public void define(DeclNode entity);

    abstract public Scope getParentScope();

    abstract public String getKind();
    abstract public String getName();

    abstract public DeclNode resolve(String name);
    abstract public DeclNode resolveThis();
    abstract public boolean resolveBreakContinue();
    abstract public boolean resolveReturn(ReturnStmtNode node);
}
