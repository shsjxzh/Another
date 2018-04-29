package shsjxzh.compiler.FrontEnd;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import shsjxzh.compiler.AST.*;
import shsjxzh.compiler.AST.Expr.ExprNode;
import shsjxzh.compiler.AST.tool.Position;
import shsjxzh.compiler.Parser.*;
import shsjxzh.compiler.Type.*;
//import shsjxzh.compiler.Unuse.Type.Type;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ASTBuilder extends MxBaseVisitor<ASTNode> {
    private ProgramNode program;        //the root of AST
    public ProgramNode getProgram() {return program;}

    // Start
    @Override
    public ASTNode visitProgram(MxParser.ProgramContext ctx) {
        List<DeclNode> declnodes = new ArrayList<>();
        Position progPos = new Position(ctx);
        //ctx.decl().
        for (ParseTree child : ctx.decl()){
            DeclNode childNode = (DeclNode) visit(child);
            declnodes.add(childNode);
        }
        program = new ProgramNode(progPos,declnodes);
        return program;
    }

    // Decl
    // varDecl
    @Override
    public ASTNode visitVardecl(MxParser.VardeclContext ctx) {
        MxParser.VarDeclContext body = ctx.varDecl();
        String name = body.ID().getText();
        Position varPos = new Position(body.ID());
        TypeNode varType = (TypeNode) visit(body.type());

        ExprNode init = null;
        if (body.varInitializer() != null){
            init = (ExprNode) visit(body.varInitializer());
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
            else if (childNode instanceof VarDeclNode){
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

}
