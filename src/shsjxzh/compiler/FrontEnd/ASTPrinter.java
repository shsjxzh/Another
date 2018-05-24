package shsjxzh.compiler.FrontEnd;

import shsjxzh.compiler.AST.*;
import shsjxzh.compiler.AST.Decl.ClassDeclNode;
import shsjxzh.compiler.AST.Decl.DeclNode;
import shsjxzh.compiler.AST.Decl.FuncDeclNode;
import shsjxzh.compiler.AST.Decl.VarDeclNode;
import shsjxzh.compiler.AST.Expr.*;
import shsjxzh.compiler.AST.Stmt.*;
import shsjxzh.compiler.AST.Expr.SuffixNode;

import java.io.PrintStream;
import java.util.List;

public class ASTPrinter implements ASTVisitor {
    private String indent;
    private PrintStream out;

    public ASTPrinter(PrintStream out) {
        indent = "";
        this.out = out;
    }

    //tool
    private void addIndent() {indent += "\t";}
    private void subIndent() {indent = indent.substring(1);}
    private void myPrintln(String s) {this.out.println(indent + s);}

    @Override
    public void visit(ProgramNode node) {
        myPrintln("ProgramNode:");
        myPrintln("ProgramNode:");
        List<DeclNode> decls = node.getDeclnodes();
        for (DeclNode decl : decls) {
            decl.accept(this);
        }
        
    }

    //tested
    @Override
    public void visit(FuncDeclNode node) {
        addIndent();
        myPrintln("FuncDeclNode: ");
        myPrintln("name: "+ node.getName());

        myPrintln("function return type: ");
        if (node.getFuncReturnType() != null){
            myPrintln("\t" + node.getFuncReturnType().toString());
        }
        else myPrintln("\tvoid");

        myPrintln("function params:");
        List<VarDeclNode> params = node.getFuncParams();

        for (VarDeclNode param : params) {
            param.accept(this);
        }

        myPrintln("function body: ");
        if (node.getFuncBlock() != null) node.getFuncBlock().accept(this);

        subIndent(); 
    }

    //tested
    @Override
    public void visit(ClassDeclNode node) {
        addIndent();
        myPrintln("ClassDeclNode:");
        myPrintln("name: " + node.getName());

        myPrintln("Constract method: ");
        if (node.getConstructMethod() != null){
            node.getConstructMethod().accept(this);
        }

        myPrintln("class member: ");
        List<VarDeclNode> members = node.getClassMember();

        for (VarDeclNode member : members) {
            member.accept(this);
        }

        myPrintln("class method: ");
        List<FuncDeclNode> methods = node.getClassMethod();
        for (FuncDeclNode method : methods) {
            method.accept(this);
        }

        subIndent(); 
    }

    //tested
    @Override
    public void visit(VarDeclNode node) {
        addIndent();
        myPrintln("VarDeclNode:");
        myPrintln("name: " + node.getName());
        myPrintln("type:");
        myPrintln("\t" + node.getVarType().toString());

        myPrintln("init expr:");
        if (node.getExpr() != null){
            node.getExpr().accept(this);
        }
        subIndent(); 
    }

    //tested
    @Override
    public void visit(BlockNode node) {
        addIndent();
        myPrintln("BlockNode:");
        List<StmtNode> stmts = node.getStmt();
        for (StmtNode stmt : stmts) {
            stmt.accept(this);
        }
        subIndent(); 
    }

    @Override
    public void visit(BreakStmtNode node) {
        addIndent();
        myPrintln("BreakStmtNode");
        subIndent(); 
    }

    @Override
    public void visit(ContinueStmtNode node) {
        addIndent();
        myPrintln("ContinueStmtNode");
        subIndent(); 
    }

    @Override
    public void visit(ExprStmtNode node) {
        addIndent();
        myPrintln("ExprStmtNode:");

        node.expr.accept(this);

        subIndent(); 
    }

    //tested
    @Override
    public void visit(ForStmtNode node) {
        addIndent();
        myPrintln("ForStmtNode:");

        myPrintln("begin: ");
        if (node.getBegin_expr() != null){
            node.getBegin_expr().accept(this);
        }
        if (node.getBegin_varDecl() != null){
            node.getBegin_varDecl().accept(this);
        }

        myPrintln("cond: "); if (node.getCond() != null) node.getCond().accept(this);
        myPrintln("step: "); if (node.getIter() != null) node.getIter().accept(this);
        myPrintln("body: "); if (node.getBody() != null) node.getBody().accept(this);

        subIndent(); 
    }

    //tested
    @Override
    public void visit(IfStmtNode node) {
        addIndent();
        myPrintln("IfStmtNode:");

        myPrintln("cond: "); node.getCond().accept(this);
        myPrintln("then: "); node.getThen().accept(this);
        myPrintln("otherwise: "); node.getOtherwise().accept(this);

        subIndent(); 
    }

    @Override
    public void visit(VarDeclStmtNode node) {
        addIndent();
        myPrintln("VarDeclStmtNode:");
        node.getVarDecl().accept(this);
        subIndent(); 
    }

    @Override
    public void visit(ReturnStmtNode node) {
        addIndent();
        myPrintln("ReturnStmtNode");
        subIndent(); 
    }

    //tested
    @Override
    public void visit(WhileStmtNode node) {
        addIndent();
        myPrintln("WhileStmtNode:");
        myPrintln("cond: "); node.getCond().accept(this);
        myPrintln("body: "); node.getBody().accept(this);
        subIndent(); 
    }

    @Override
    public void visit(BoolLiteralNode node) {
        addIndent();
        myPrintln("BoolLiteralNode: " + "bool value: " + node.getValue());
        subIndent(); 
    }

    @Override
    public void visit(IntLiteralNode node) {
        addIndent();
        myPrintln("IntlLiteralNode: " + "int value: " + node.getValue());
        subIndent(); 
    }

    @Override
    public void visit(NullLiteralNode node) {
        addIndent();
        myPrintln("nullLiteralNode");
        subIndent(); 
    }

    @Override
    public void visit(StringLiteralNode node) {
        addIndent();
        myPrintln("StringLiteralNode: " + "value: " + node.getValue());
        subIndent(); 
    }

    @Override
    public void visit(BinaryOpNode node) {
        addIndent();
        myPrintln("BinaryOpNode:");
        myPrintln("op: " + node.getOp());
        myPrintln("left: "); node.getLeft().accept(this);
        myPrintln("right: "); node.getRight().accept(this);

        subIndent(); 
    }

    @Override
    public void visit(UnaryNode node) {
        addIndent();
        myPrintln("UnaryNode:");
        myPrintln("op: " + node.getOp());
        myPrintln("body"); node.getBody().accept(this);
        subIndent(); 
    }

    @Override
    public void visit(SuffixNode node) {
        addIndent();
        myPrintln("SuffixNode:");
        myPrintln("op: " + node.getOp());
        myPrintln("body"); node.getBody().accept(this);
        subIndent(); 
    }

    @Override
    public void visit(ArrayIndexNode node) {
        addIndent();
        myPrintln("ArrayIndexNode:");
        myPrintln("array: "); node.getArray().accept(this);
        myPrintln("index: "); node.getIndex().accept(this);
        subIndent(); 
    }

    @Override
    public void visit(VariableNode node) {
        addIndent();
        myPrintln("VariableNode:");
        myPrintln("name: " + node.getName());
        subIndent(); 
    }

    @Override
    public void visit(CallNode node) {
        addIndent();
        myPrintln("CallNode:");
        myPrintln("function name: "+ node.getFuncName());
        myPrintln("params: ");
        List<ExprNode> params = node.getFuncParams();
        for (ExprNode param : params) {
            param.accept(this);
        }
        subIndent(); 
    }

    @Override
    public void visit(MemberAccessNode node) {
        addIndent();
        myPrintln("MemberAccessNode:");
        myPrintln("object: "); node.getObject().accept(this);
        myPrintln("memberRef: " + node.getMemberRef());
        subIndent(); 
    }

    @Override
    public void visit(MethodAccessNode node) {
        addIndent();
        myPrintln("MethodAccessNode:");
        myPrintln("object: "); node.getObject().accept(this);
        myPrintln("method name:" + node.getMethodName());
        myPrintln("method params: ");
        List<ExprNode> methodParams = node.getMethodParams();
        for (ExprNode methodParam : methodParams) {
            methodParam.accept(this);
        }
        subIndent(); 
    }

    @Override
    public void visit(NewNode node) {
        addIndent();
        myPrintln("NewNode:");
        myPrintln("type: "); myPrintln(node.getExprType().toString());
        myPrintln("expr dim: ");
        List<ExprNode> exprDims = node.getExprDim();
        for (ExprNode exprDim : exprDims) {
            exprDim.accept(this);
        }

        subIndent(); 
    }

    @Override
    public void visit(ThisNode node) {
        addIndent();
        myPrintln("ThisNode");
        subIndent(); 
    }
}
