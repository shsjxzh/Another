package shsjxzh.compiler.FrontEnd;

import org.antlr.v4.runtime.tree.ParseTree;
import shsjxzh.compiler.AST.*;
import shsjxzh.compiler.AST.Decl.ClassDeclNode;
import shsjxzh.compiler.AST.Decl.DeclNode;
import shsjxzh.compiler.AST.Decl.FuncDeclNode;
import shsjxzh.compiler.AST.Decl.VarDeclNode;
import shsjxzh.compiler.AST.Expr.*;
import shsjxzh.compiler.AST.Stmt.*;
import shsjxzh.compiler.AST.tool.Position;
import shsjxzh.compiler.Parser.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ASTBuilder extends MxBaseVisitor<ASTNode> {
    // Start
    @Override
    public ASTNode visitProgram(MxParser.ProgramContext ctx) {
        List<DeclNode> declnodes = new LinkedList<>();
        Position progPos = new Position(ctx);
        for (ParseTree child : ctx.decl()){
            DeclNode childNode = (DeclNode) visit(child);
            declnodes.add(childNode);
        }
        return new ProgramNode(progPos,declnodes);
    }

    // Decl
    // varDecl
    @Override
    public ASTNode visitFieldVarDecl(MxParser.FieldVarDeclContext ctx) {
        return visit(ctx.varDecl());
    }

    @Override
    public ASTNode visitVarDecl(MxParser.VarDeclContext ctx) {
        String name = ctx.ID().getText();
        Position varPos = new Position(ctx.ID());
        TypeNode varType = (TypeNode) visit(ctx.type());

        ExprNode init = null;
        if (ctx.varInitializer() != null){
            init = (ExprNode) visit(ctx.varInitializer());
        }

        return new VarDeclNode(varPos, varType, name, init);
    }

    @Override
    public ASTNode visitVarInitializer(MxParser.VarInitializerContext ctx) {
        return this.visit(ctx.expr());
    }

    // funcDecl
    @Override
    public ASTNode visitFuncDecl(MxParser.FuncDeclContext ctx) {
        Position funcPos = new Position(ctx);
        TypeNode funcReturnType = null;

        if (ctx.type() != null) {
            funcReturnType = (TypeNode) visit(ctx.type());
        }

        String funcName = ctx.ID().getText();

        List<VarDeclNode> funcParams = new LinkedList<>();
        //注意这里“？”是否返回一个空指针
        if (ctx.formalParameters().varDecl() != null) {
            for (ParseTree param : ctx.formalParameters().varDecl()) {
                funcParams.add((VarDeclNode) visit(param));
            }
        }

        BlockNode funcBody = (BlockNode) visit(ctx.blockStat());
        return new FuncDeclNode(funcPos, funcReturnType, funcBody ,funcName, funcParams);
    }

    //classDecl
    @Override
    public ASTNode visitClassDecl(MxParser.ClassDeclContext ctx) {
        Position classPos = new Position(ctx);
        String className = ctx.ID().getText();

        List<VarDeclNode> classMember = new LinkedList<>();
        List<FuncDeclNode> classMethod = new LinkedList<>();
        for (ParseTree child: ctx.classStat()){
            ASTNode childNode = visit(child);
            if (childNode instanceof VarDeclNode){
                classMember.add((VarDeclNode) childNode);
            }
            else if (childNode instanceof FuncDeclNode){
                classMethod.add((FuncDeclNode) childNode);
            }
        }

        FuncDeclNode constructMethod = null;
        if (ctx.constractDecl() != null){
            Position funcPos = new Position(ctx);
            String funcName = ctx.constractDecl().ID().getText();
            BlockNode funcBody = (BlockNode) visit(ctx.constractDecl().blockStat());
            constructMethod = new FuncDeclNode(funcPos, null, funcBody ,funcName, null);
        }

        return new ClassDeclNode(classPos, className, classMember, classMethod, constructMethod);
    }

    //type
    @Override
    public ASTNode visitType(MxParser.TypeContext ctx) {
        Position typePos = new Position(ctx);
        TypeNode typeNode = new TypeNode(typePos);
        typeNode.setType(ctx.nonArrayType().getText(), ctx.LBRACK().size());
        return typeNode;
    }

    //Stmt
    @Override
    public ASTNode visitBlockStat(MxParser.BlockStatContext ctx) {
        Position blockPos = new  Position(ctx);
        List<StmtNode> blockStmt = new LinkedList<>();
        for (ParseTree child : ctx.stat()){
            //如果遇到空语句，返回值应该是null
            //需要测试";"语句
            ASTNode childNode = visit(child);
            if (childNode instanceof StmtNode){
                blockStmt.add((StmtNode) childNode);
            }
        }
        return new BlockNode(blockPos, blockStmt);
    }

    @Override
    public ASTNode visitExprStat(MxParser.ExprStatContext ctx) {
        ExprNode expr = (ExprNode) visit(ctx.expr());
        return new ExprStmtNode(new Position(ctx), expr);
    }

    @Override
    public ASTNode visitSelectStat(MxParser.SelectStatContext ctx) {
        Position ifPos = new Position(ctx);
        ExprNode ifCond = (ExprNode) visit(ctx.judgeExpr().expr());
        StmtNode ifBody = (StmtNode) visit(ctx.stat());

        List<ExprNode> elseIfCond = new LinkedList<>();
        List<StmtNode> elseIfBody = new LinkedList<>();

        for (MxParser.ElseIfBodyContext child : ctx.elseIfBody()) {
            elseIfCond.add( (ExprNode) visit(child.judgeExpr().expr()));
            elseIfBody.add( (StmtNode) visit(child.stat()));
        }

        StmtNode elseBody = null;
        if (ctx.elseBody() != null){
            elseBody = (StmtNode) visit(ctx.elseBody().stat());
        }
        return new IfStmtNode(ifPos, ifCond, ifBody, elseIfCond, elseIfBody, elseBody);
    }

    @Override
    public ASTNode visitVarDeclStat(MxParser.VarDeclStatContext ctx) {
        VarDeclNode varDecl = (VarDeclNode) visit(ctx.varDecl());
        return new VarDeclStmtNode(new Position(ctx),varDecl);
    }

    @Override
    public ASTNode visitFor(MxParser.ForContext ctx) {
        Position forPos = new Position(ctx);
        ExprNode begin_expr = null;
        if (ctx.initE != null){
            begin_expr = (ExprNode) visit(ctx.initE);
        }
        VarDeclNode begin_varDecl = null;
        if (ctx.initV != null){
            begin_varDecl = (VarDeclNode) visit(ctx.initV);
        }
        ExprNode cond = null;
        if (ctx.cond != null){
            cond = (ExprNode) visit(ctx.cond);
        }
        ExprNode iter = null;
        if (ctx.step != null){
            iter = (ExprNode) visit(ctx.step);
        }
        StmtNode body = (StmtNode) visit(ctx.stat());

        return new ForStmtNode(forPos, begin_expr, begin_varDecl, cond, iter, body);
    }

    @Override
    public ASTNode visitWhile(MxParser.WhileContext ctx) {
        Position whilePos = new Position(ctx);
        ExprNode cond = (ExprNode) visit(ctx.judgeExpr().expr());
        StmtNode body = (StmtNode) visit(ctx.stat());
        return new WhileStmtNode(whilePos, cond, body);
    }

    @Override
    public ASTNode visitBreak(MxParser.BreakContext ctx) {
        return new BreakStmtNode(new Position(ctx));
    }

    @Override
    public ASTNode visitContinue(MxParser.ContinueContext ctx) {
        return new ContinueStmtNode(new Position(ctx));
    }

    @Override
    public ASTNode visitReturn(MxParser.ReturnContext ctx) {
        return new ReturnStmtNode(new Position(ctx), (ExprNode) visit(ctx.expr()));
    }

    @Override
    public ASTNode visitBinary(MxParser.BinaryContext ctx) {
        BinaryOpNode.BinaryOp op;
        switch (ctx.op.getType()){
            case MxParser.ADD: op = BinaryOpNode.BinaryOp.ADD; break;
            case MxParser.SUB: op = BinaryOpNode.BinaryOp.SUB; break;
            case MxParser.MUL: op = BinaryOpNode.BinaryOp.MUL; break;
            case MxParser.DIV: op = BinaryOpNode.BinaryOp.DIV; break;
            case MxParser.MOD: op = BinaryOpNode.BinaryOp.MOD; break;

            case MxParser.GT: op = BinaryOpNode.BinaryOp.GT; break;
            case MxParser.LT: op = BinaryOpNode.BinaryOp.LT; break;
            case MxParser.EQ: op = BinaryOpNode.BinaryOp.EQ; break;
            case MxParser.LE: op = BinaryOpNode.BinaryOp.LE; break;
            case MxParser.GE: op = BinaryOpNode.BinaryOp.GE; break;
            case MxParser.NEQ: op = BinaryOpNode.BinaryOp.NEQ; break;

            case MxParser.LOG_AND: op = BinaryOpNode.BinaryOp.LOG_AND; break;
            case MxParser.LOG_OR: op = BinaryOpNode.BinaryOp.LOG_OR; break;

            case MxParser.L_SHIFT: op = BinaryOpNode.BinaryOp.L_SHIFT; break;
            case MxParser.R_SHIFT: op = BinaryOpNode.BinaryOp.R_SHIFT; break;

            case MxParser.BIT_AND: op = BinaryOpNode.BinaryOp.BIT_AND; break;
            case MxParser.BIT_OR: op = BinaryOpNode.BinaryOp.BIT_OR; break;
            case MxParser.BIT_XOR: op = BinaryOpNode.BinaryOp.XOR; break;

            case MxParser.ASSIGN: op = BinaryOpNode.BinaryOp.ASSIGN; break;

            default: throw new RuntimeException("Unknown binary operator");
        }

        Position binaryPos = new Position(ctx);
        ExprNode left = (ExprNode) visit(ctx.expr(0));
        ExprNode right = (ExprNode) visit(ctx.expr(1));

        return new BinaryOpNode(binaryPos, left, right, op);
    }

    @Override
    public ASTNode visitUnary(MxParser.UnaryContext ctx) {
        //都是在前部的
        UnaryNode.UnaryOp op;
        switch (ctx.op.getType()){
            case MxParser.ADD: op = UnaryNode.UnaryOp.POS; break;
            case MxParser.SUB: op = UnaryNode.UnaryOp.NEG; break;

            case MxParser.INC: op = UnaryNode.UnaryOp.INC; break;
            case MxParser.DEC: op = UnaryNode.UnaryOp.DEC; break;

            case MxParser.LOG_NOT: op = UnaryNode.UnaryOp.LOG_NOT; break;
            case MxParser.BIT_NOT: op = UnaryNode.UnaryOp.BIT_NOT; break;


            default: throw new RuntimeException("Unkonwn unary operatro");
        }
        Position unaryPos = new Position(ctx);
        ExprNode exprbody = (ExprNode) visit(ctx.expr());

        return new UnaryNode(unaryPos, op, exprbody);
    }

    @Override
    public ASTNode visitIndex(MxParser.IndexContext ctx) {
        Position indexPos = new Position(ctx);
        ExprNode array = (ExprNode) visit(ctx.expr(0));
        ExprNode index = (ExprNode) visit(ctx.expr(1));
        return new ArrayIndexNode(indexPos, array, index);
    }

    @Override
    public ASTNode visitCall(MxParser.CallContext ctx) {
        Position callPos = new Position(ctx);
        String funcName = ctx.ID().getText();
        List<ExprNode> funcParams = new LinkedList<>();
        if (ctx.exprList() != null){
            for (MxParser.ExprContext child : ctx.exprList().expr()) {
                ExprNode childNode = (ExprNode) visit(child);
                funcParams.add(childNode);
            }
        }
        return new CallNode(callPos, funcName, funcParams);
    }

    @Override
    public ASTNode visitFieldAccess(MxParser.FieldAccessContext ctx) {
        Position memberPos = new Position(ctx);
        ExprNode object = (ExprNode) visit(ctx.expr());
        String member = ctx.ID().getText();

        return new MemberAccessNode(memberPos, object, member);
    }

    @Override
    public ASTNode visitClassFuncAccess(MxParser.ClassFuncAccessContext ctx) {
        Position methodPos = new Position(ctx);
        ExprNode object = (ExprNode) visit(ctx.expr());

        String methodName = ctx.ID().getText();
        List<ExprNode> methodParams = new LinkedList<>();
        if (ctx.exprList() != null){
            for (MxParser.ExprContext child : ctx.exprList().expr()) {
                ExprNode childNode = (ExprNode) visit(child);
                methodParams.add(childNode);
            }
        }
        return new MethodAccessNode(methodPos, object, methodName, methodParams);
    }

    @Override
    public ASTNode visitBracket(MxParser.BracketContext ctx) {
        return visit(ctx.expr());
    }

    /*Todo:
        后缀操作啦，new啦，常量啦，this啦

     */
}
