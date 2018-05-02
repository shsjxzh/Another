package shsjxzh.compiler.FrontEnd;

import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import shsjxzh.compiler.AST.*;
import shsjxzh.compiler.AST.Decl.ClassDeclNode;
import shsjxzh.compiler.AST.Decl.DeclNode;
import shsjxzh.compiler.AST.Decl.FuncDeclNode;
import shsjxzh.compiler.AST.Decl.VarDeclNode;
import shsjxzh.compiler.AST.Expr.*;
import shsjxzh.compiler.AST.Stmt.*;
import shsjxzh.compiler.AST.tool.Position;
import shsjxzh.compiler.ErrorHandle.ErrorHandler;
import shsjxzh.compiler.Parser.*;
import shsjxzh.compiler.Type.Type;

import java.util.ArrayList;
//import java.util.LinkedList;
import java.util.List;

public class ASTBuilder extends MxBaseVisitor<ASTNode> {
    // Start
    private void initialize(Position pos, List<DeclNode> declNodes){
        //将内置类型，函数等在这个时候加入
        //int
        declNodes.add( new ClassDeclNode(pos,"int",new ArrayList<>(),
                new ArrayList<>(),null) );

        //string
        List<FuncDeclNode> stringMethod = new ArrayList<>();
        //int length()
        FuncDeclNode length = new FuncDeclNode(pos, new Type("int", 0), null, "length", new ArrayList<>());
        stringMethod.add(length);

        //int parseInt()
        FuncDeclNode parseInt = new FuncDeclNode(pos, new Type("int",0), null, "parseInt", new ArrayList<>());
        stringMethod.add(parseInt);

        //string subString(int left, int right)
        List<VarDeclNode> subParams = new ArrayList<>();
        subParams.add(new VarDeclNode(pos,new Type("int",0), "left",null));
        subParams.add(new VarDeclNode(pos, new Type("int", 0), "right",null));
        FuncDeclNode substring = new FuncDeclNode(pos, new Type("string",0), null, "substring", subParams);
        stringMethod.add(substring);

        //int ord(int pos)
        List<VarDeclNode> ordParams = new ArrayList<>();
        ordParams.add(new VarDeclNode(pos, new Type("int", 0), "pos",null));
        FuncDeclNode ord = new FuncDeclNode(pos, new Type("int",0), null, "ord", ordParams);
        stringMethod.add(ord);

        declNodes.add( new ClassDeclNode(pos, "string", new ArrayList<>(), stringMethod, null));

        //bool
        declNodes.add( new ClassDeclNode(pos,"bool",new ArrayList<>(),
                new ArrayList<>(),null) );

        //Todo: 完成剩下的几个函数加入
        //void print(string str)
        List<VarDeclNode> printParams = new ArrayList<>();
        printParams.add(new VarDeclNode(pos, new Type("string",0), "str", null));
        declNodes.add( new FuncDeclNode(pos,null, null,"print", printParams ));

        //void println(string str)
        List<VarDeclNode> printlnParams = new ArrayList<>();
        printlnParams.add(new VarDeclNode(pos, new Type("string",0), "str", null));
        declNodes.add( new FuncDeclNode(pos,null, null,"println", printlnParams) );

        //string getString()
        declNodes.add( new FuncDeclNode(pos,new Type("string",0), null,"getString", new ArrayList<>()));

        //int getInt()
        declNodes.add (new FuncDeclNode(pos, new Type("int",0), null, "getInt",new ArrayList<>()));

        //string toString(int i)
        List<VarDeclNode> toParams = new ArrayList<>();
        toParams.add(new VarDeclNode(pos,new Type("int",0),"i", null));
        declNodes.add(new FuncDeclNode(pos, new Type("string",0), null, "toString", toParams));

    }

    @Override
    public ASTNode visitProgram(MxParser.ProgramContext ctx) {
        List<DeclNode> declNodes = new ArrayList<>();
        Position progPos = new Position(ctx);
        for (ParseTree child : ctx.decl()){
            DeclNode childNode = (DeclNode) visit(child);
            declNodes.add(childNode);
        }

        initialize(progPos,declNodes);

        return new ProgramNode(progPos,declNodes);
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
        Position varPos = new Position(ctx);
        Type varType = new Type(ctx.type().nonArrayType().getText(), ctx.type().LBRACK().size());

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
        Type funcReturnType = null;

        if (ctx.type() != null) {
            funcReturnType = new Type(ctx.type().nonArrayType().getText(), ctx.type().LBRACK().size());
        }

        String funcName = ctx.ID().getText();

        List<VarDeclNode> funcParams = new ArrayList<>();
        //pay attention to "?"
        if (ctx.formalParameters() != null) {
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

        List<VarDeclNode> classMember = new ArrayList<>();
        List<FuncDeclNode> classMethod = new ArrayList<>();
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
            Position funcPos = new Position(ctx.constractDecl());
            String funcName = ctx.constractDecl().ID().getText();

            //这里做一个小的构造函数名检查
            if (!funcName.equals(className)){
                throw new ErrorHandler("Invalid constructor ", funcPos);
            }

            BlockNode funcBody = (BlockNode) visit(ctx.constractDecl().blockStat());
            constructMethod = new FuncDeclNode(funcPos, null, funcBody ,funcName, new ArrayList<>());
        }

        return new ClassDeclNode(classPos, className, classMember, classMethod, constructMethod);
    }

    //Stmt
    @Override
    public ASTNode visitBlockStat(MxParser.BlockStatContext ctx) {
        Position blockPos = new  Position(ctx);
        List<StmtNode> blockStmt = new ArrayList<>();
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
        StmtNode otherwise = (StmtNode) visit(ctx.elseBody().stat());

        return new IfStmtNode(ifPos, ifCond, ifBody, otherwise);
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

    // Expr
    @Override
    public ASTNode visitBinary(MxParser.BinaryContext ctx) {
        Position binaryPos = new Position(ctx);
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

            default: throw new ErrorHandler("Unknown binary operator", binaryPos);
        }

        ExprNode left = (ExprNode) visit(ctx.expr(0));
        ExprNode right = (ExprNode) visit(ctx.expr(1));

        return new BinaryOpNode(binaryPos, left, right, op);
    }

    @Override
    public ASTNode visitUnary(MxParser.UnaryContext ctx) {
        //都是在前部的
        Position unaryPos = new Position(ctx);
        UnaryNode.UnaryOp op;
        switch (ctx.op.getType()){
            case MxParser.ADD: op = UnaryNode.UnaryOp.POS; break;
            case MxParser.SUB: op = UnaryNode.UnaryOp.NEG; break;

            case MxParser.INC: op = UnaryNode.UnaryOp.INC; break;
            case MxParser.DEC: op = UnaryNode.UnaryOp.DEC; break;

            case MxParser.LOG_NOT: op = UnaryNode.UnaryOp.LOG_NOT; break;
            case MxParser.BIT_NOT: op = UnaryNode.UnaryOp.BIT_NOT; break;

            default: throw new ErrorHandler("Unkonwn unary operator", unaryPos);
        }

        ExprNode exprbody = (ExprNode) visit(ctx.expr());

        return new UnaryNode(unaryPos, op, exprbody);
    }

    @Override
    public ASTNode visitPostfixSelfAddSub(MxParser.PostfixSelfAddSubContext ctx) {
        Position suffixPos = new Position(ctx);
        SuffixNode.UnaryOp op;
        switch (ctx.op.getType()){
            case MxParser.INC: op = SuffixNode.UnaryOp.INC; break;
            case MxParser.DEC: op = SuffixNode.UnaryOp.DEC; break;
            default: throw new ErrorHandler("Unknown suffix operator", suffixPos);
        }

        ExprNode exprBody = (ExprNode) visit(ctx.expr());

        return new SuffixNode(suffixPos, op, exprBody);
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
        List<ExprNode> funcParams = new ArrayList<>();
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
        List<ExprNode> methodParams = new ArrayList<>();
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

    @Override
    public ASTNode visitConstant(MxParser.ConstantContext ctx) {
        return visit(ctx.literal());
    }

    @Override
    public ASTNode visitLiteral(MxParser.LiteralContext ctx) {
        Position literalPos = new Position(ctx);
        //此type里包含了常量的信息
        Token type = ctx.literalType;
        if (type.getType() == MxParser.NULL_LITERAL){
            return new NullLiteralNode(literalPos);
        }
        else if (type.getType() == MxParser.BOOL_LITERAL){
            return  new BoolLiteralNode(literalPos, type.getText().equals("true"));
        }
        else if (type.getType() == MxParser.INT_LITERAL){
            return new IntLiteralNode(literalPos, Integer.parseInt(type.getText()));
        }
        else if (type.getType() == MxParser.STRING_LITERAL){
            return new StringLiteralNode(literalPos, type.getText());
        }
        else{
            throw new ErrorHandler("Invalid Literal",literalPos);
        }
    }

    @Override
    public ASTNode visitThis(MxParser.ThisContext ctx) {
        return new ThisNode(new Position(ctx));
    }

    @Override
    public ASTNode visitIDLeaf(MxParser.IDLeafContext ctx) {
        return new VariableNode(new Position(ctx), ctx.getText());
    }

    //new
    @Override
    public ASTNode visitNew(MxParser.NewContext ctx) {
        return visit(ctx.creator());
    }

    @Override
    public ASTNode visitCreator(MxParser.CreatorContext ctx) {
        Position creatorPos = new Position(ctx);

        List<ExprNode> exprDim = new ArrayList<>();
        if (ctx.newDim()!= null) {
            for (MxParser.ExprContext child : ctx.newDim().expr()) {
                ExprNode childNode = (ExprNode) visit(child);
                exprDim.add(childNode);
            }
        }
        Type type = new Type(ctx.nonArrayType().getText(), 0);
        if (ctx.newDim() != null) {
            //nonExprDim = ctx.newDim().LBRACK().size() - exprDim.size();
            // new操作中的type的dim被定义为整个new出来的数组大小
            type.setDim( ctx.newDim().LBRACK().size() );
        }
        return new NewNode(creatorPos, type, exprDim);
    }
}
