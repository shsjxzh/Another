package shsjxzh.compiler.AST.Stmt;

import shsjxzh.compiler.AST.ASTVisitor;
import shsjxzh.compiler.AST.Expr.ExprNode;
import shsjxzh.compiler.AST.Decl.VarDeclNode;
import shsjxzh.compiler.AST.tool.Position;

public class ForStmtNode extends StmtNode {
    private ExprNode begin_expr;
    private VarDeclNode begin_varDecl;
    private ExprNode cond;
    private ExprNode iter;
    private StmtNode body;

    public ForStmtNode(Position pos, ExprNode begin_expr, VarDeclNode begin_varDecl, ExprNode cond, ExprNode iter, StmtNode body) {
        super(pos);
        this.begin_expr = begin_expr;
        this.begin_varDecl = begin_varDecl;
        this.cond = cond;
        this.iter = iter;
        this.body = body;
    }

    public ExprNode getBegin_expr() {
        return begin_expr;
    }

    public VarDeclNode getBegin_varDecl() {
        return begin_varDecl;
    }

    public ExprNode getCond() {
        return cond;
    }

    public ExprNode getIter() {
        return iter;
    }

    public StmtNode getBody() {
        return body;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
