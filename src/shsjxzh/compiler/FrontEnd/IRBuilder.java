package shsjxzh.compiler.FrontEnd;

import shsjxzh.compiler.AST.ASTNode;
import shsjxzh.compiler.AST.ASTVisitor;
import shsjxzh.compiler.AST.Decl.*;
import shsjxzh.compiler.AST.Expr.*;
import shsjxzh.compiler.AST.ProgramNode;
import shsjxzh.compiler.AST.Stmt.*;
import shsjxzh.compiler.IR.BasicBlock;
import shsjxzh.compiler.IR.Function;
import shsjxzh.compiler.IR.IRRoot;
import shsjxzh.compiler.IR.Instruction.*;
import shsjxzh.compiler.IR.Value.*;
import shsjxzh.compiler.Type.Type;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class IRBuilder implements ASTVisitor {
    BasicBlock curBB;
    Function curFunc;

    BasicBlock curReturnBB;
    VirtualRegister curReturnReg;

    BasicBlock curBreakLoopBB;
    BasicBlock curContinueLoopBB;

    int intSize = 8;

    boolean isFuncParam = false;

    boolean inSideEffect = false;

    boolean LValueGetAddress = false;

    private IRRoot irRoot = new IRRoot();

    public IRRoot getIrRoot() {
        return irRoot;
    }

    /*
    ========================= Global Tool Function =========================
    */
    private void setShortCircuitLeaf(ExprNode node){
        curBB.finish(new Branch(curBB, node.regOrImm, node.ifTrue.getName(),node.ifFalse.getName()));
        curBB.LinkNextBB(node.ifTrue);
        curBB.LinkNextBB(node.ifFalse);
        curBB.setAdjacentBB(node.ifTrue);
    }

    private void generateIR(ASTNode node){
        if (node != null){
            //eliminate the dead expr
            if (node instanceof BinaryOpNode){
                if (((BinaryOpNode)node).getOp() != BinaryOpNode.BinaryOp.ASSIGN){
                    if (inSideEffect) node.accept(this);
                }
                else node.accept(this);
            }
            else if (node instanceof UnaryNode){
                if (((UnaryNode) node).getOp() != UnaryNode.UnaryOp.INC && ((UnaryNode) node).getOp() != UnaryNode.UnaryOp.DEC){
                    if (inSideEffect) node.accept(this);
                }
                else node.accept(this);
            }
            else node.accept(this);
        }
    }

    private boolean inLogicalGeneration(ExprNode node){
        return node.ifTrue != null;
    }

    private boolean needMemAccess(ExprNode node){
        if (node instanceof ArrayIndexNode || node instanceof MemberAccessNode) return true;
        else if (node instanceof VariableNode && ((VariableNode) node).inClassVar != null) return true;
        else return false;
    }

    /*
    ========================= Tool Function for global variable =========================
    */

    private void staticVarGenerate(VarDeclNode node){
        //set SideEffect
        boolean oldInSideEffect = inSideEffect;
        inSideEffect = true;

        StaticData data = new StaticSpace(node.getName(), node.getVarType().getRegisterSize());
        //there is no problem when it comes to string, because "stringLiteral" will handle everything
        irRoot.staticDataList.add(data);

        //VirtualRegister reg = new VirtualRegister("Reg_" + node.getName());
        //curFunc.addFuncLocalVar(reg);
        irRoot.getRegCountAndIncrease();
        //it is not Expr!!
        node.varReg = data;

        if (node.getExpr() != null){
            //check this!!
            generateIR(node.getExpr());
            //tmp operation
            //change when Memory operation is needed
            //curBB.append(new Move(curBB, reg, node.getExpr().regOrImm));
            assignNonMemop(data, node.getExpr().regOrImm, node.getExpr());
        }
        else{
            curBB.append(new Move(curBB, data, new IntImme(0)));
        }

        //exit SideEffect
        inSideEffect = oldInSideEffect;
    }

    /*
    ========================= Tool Function for build-in function =========================
    */
    private void insertBuildInFunc(){
        Function tmp;
        //build-in function has two "_" at the front
        tmp = new Function("__PrintString");
        irRoot.buildInFunctionMap.put(tmp.getName(), tmp);
        tmp = new Function("__PrintlnString");
        irRoot.buildInFunctionMap.put(tmp.getName(), tmp);
        tmp = new Function("__PrintInt");
        irRoot.buildInFunctionMap.put(tmp.getName(), tmp);
        tmp = new Function("__PrintlnInt");
        irRoot.buildInFunctionMap.put(tmp.getName(), tmp);
        tmp = new Function("__ToString");
        irRoot.buildInFunctionMap.put(tmp.getName(), tmp);
        tmp = new Function("__GetString");
        irRoot.buildInFunctionMap.put(tmp.getName(), tmp);
        tmp = new Function("__GetInt");
        irRoot.buildInFunctionMap.put(tmp.getName(), tmp);
        tmp = new Function("__StringConcat");
        irRoot.buildInFunctionMap.put(tmp.getName(), tmp);

        tmp = new Function("__StringEqual");
        irRoot.buildInFunctionMap.put(tmp.getName(), tmp);
        tmp = new Function("__StringLess");
        irRoot.buildInFunctionMap.put(tmp.getName(), tmp);

        tmp = new Function("__StringParseInt");
        irRoot.buildInFunctionMap.put(tmp.getName(), tmp);
        tmp = new Function("__StringSubString");
        irRoot.buildInFunctionMap.put(tmp.getName(), tmp);
        tmp = new Function("__StringOrd");
        irRoot.buildInFunctionMap.put(tmp.getName(), tmp);
    }

    /*
    ========================= Tool Function for function preprocess =========================
    */
    private void preProcessFunc(FuncDeclNode node){
        if (node != null) {
            Function tmp;
            if (node.className == null) {
                tmp = new Function(node.getName(), "B_" + irRoot.getBBCountAndIncrease(), node.returnNum, node.getFuncReturnType().getRegisterSize());
            }
            else{
                //pay attention to this!!
                //class method has one "_" at the front
                tmp = new Function("_" + node.className + node.getName(), "B_" + irRoot.getBBCountAndIncrease(), node.returnNum, node.getFuncReturnType().getRegisterSize());
            }
            irRoot.functionMap.put(tmp.getName(), tmp);
            node.irFunction = tmp;
        }
    }

    /*
    ========================= Root Node =========================
    */
    @Override
    public void visit(ProgramNode node) {
        /*  ToDo: all the nodes that may be the leaf of condition must be carefully handled,
            list:
            arrayIndex, compare op, bool const, call(func & class method), member access, unary op("!")
        */
        //  Todo: string has some problem!!
        //  If you want to cut memory use in compiling, change jump (label) to jump (pointer)
        insertBuildInFunc();
        curFunc = irRoot.functionMap.get("__init");
        curBB = curFunc.getStartBB();


        for (DeclNode declNode : node.getDeclnodes()) {
            if (!declNode.isBuildIn) {
                if (declNode instanceof VarDeclNode) {
                    staticVarGenerate((VarDeclNode) declNode);
                } else if (declNode instanceof FuncDeclNode) {
                    preProcessFunc((FuncDeclNode) declNode);
                } else if (declNode instanceof ClassDeclNode) {
                    ClassDeclNode tmp = (ClassDeclNode) declNode;
                    preProcessFunc(tmp.getConstructMethod());
                    tmp.getClassMethod().forEach(x -> preProcessFunc(x));
                }
            }
        }

        //initialize the main first
        //generateIR(node.getMainDecl());

        for (DeclNode declNode : node.getDeclnodes()) {
            if (!declNode.isBuildIn && !(declNode instanceof VarDeclNode)) {
                //Todo: handle the build-in function and class
                generateIR(declNode);
            }
        }

        //finish the generation of "__init" function
        //pretend that there will be no parameters for "main"
        curFunc = irRoot.functionMap.get("__init");
        curBB = curFunc.getStartBB();

        VirtualRegister reg = new VirtualRegister("Reg_" + irRoot.getRegCountAndIncrease());
        curFunc.addFuncLocalVar(reg);

        curBB.append(new Call(curBB, irRoot.functionMap.get("main"), new LinkedList<>(), reg));
        //return node will point to a return basic block
        //but there is no need for a init function
        curBB.finish(new Return(curBB, reg));
    }

    /*
    ========================= Decl Node =========================
    */
    @Override
    public void visit(FuncDeclNode node) {
        if (node.className == null) {
            curFunc = irRoot.functionMap.get(node.getName());
        }
        else{
            curFunc = irRoot.functionMap.get("_" + node.className + node.getName());
        }
        //node.irFunction = curFunc;
        //already put!!
        //irRoot.functionMap.put(curFunc.getName(), curFunc);
        curBB = curFunc.getStartBB();
        //curFunc.setReturnSize(node.getFuncReturnType().getRegisterSize());

        isFuncParam = true;
        for (VarDeclNode varDeclNode : node.getFuncParams()) {
            //special handle
            generateIR(varDeclNode);
        }
        isFuncParam = false;

        //">=" -> ">" is an important optimize
        if (curFunc.getReturnStmtNum() >= 1) {
            curReturnBB = new BasicBlock("B_" + irRoot.getBBCountAndIncrease(), curFunc);
            curReturnReg = new VirtualRegister("Reg_" + irRoot.getRegCountAndIncrease());
            curFunc.addFuncLocalVar(curReturnReg);
            generateIR(node.getFuncBlock());
            curReturnBB.append(new Return(curReturnBB, curReturnReg));
        }
        else{
            //do nothing for no return
            generateIR(node.getFuncBlock());
            if (curFunc.getReturnStmtNum() == 0){
                //if the return type is not "void", then return 0
                if (curFunc.getReturnSize() > 0) curBB.finish(new Return (curBB, new IntImme(0)));
                //void
                else curBB.finish(new Return(curBB, null));
            }
            // else curBB.finish(new Return(curBB, node.getReExpr().regOrImm));
        }
    }

    @Override
    public void visit(ClassDeclNode node) {
        generateIR(node.getConstructMethod());
        node.getClassMethod().forEach(x -> generateIR(x));
    }

    @Override
    public void visit(VarDeclNode node) {
        //local variable
        //set SideEffect
        boolean oldInSideEffect = inSideEffect;
        inSideEffect = true;

        VirtualRegister reg = new VirtualRegister("Reg_" + node.getName());
        irRoot.getRegCountAndIncrease();
        node.varReg = reg;

        if (node.getExpr() != null){
            generateIR(node.getExpr());
            //tmp operation
            assignNonMemop(reg, node.getExpr().regOrImm, node.getExpr());
        }
        else if (isFuncParam){
            curFunc.addFuncParams(reg);
        }
        else{
            curBB.append(new Move(curBB, reg, new IntImme(0)));
        }

        if (!isFuncParam){
            curFunc.addFuncLocalVar(reg);
        }

        //exit SideEffect
        inSideEffect = oldInSideEffect;
    }

    /*
    ========================= Control Flow Related Stmt =========================
    */
    @Override
    public void visit(BreakStmtNode node) {
        curBB.finish(new Jump(curBB, curBreakLoopBB.getName()));
        curBB.LinkNextBB(curBreakLoopBB);
    }

    @Override
    public void visit(ContinueStmtNode node) {
        curBB.finish(new Jump(curBB, curContinueLoopBB.getName()));
        curBB.LinkNextBB(curContinueLoopBB);
    }

    @Override
    public void visit(ReturnStmtNode node) {
        //there will be a little more thing to do
        //simple optimization for "return"
        //be careful!
        //set SideEffect
        boolean oldInSideEffect = inSideEffect;
        inSideEffect = true;

        /*if (isLogicalExpr(node.getReExpr())) {
            node.getReExpr().ifTrue = new BasicBlock("B_" + irRoot.getBBCountAndIncrease(), curFunc);
            node.getReExpr().ifFalse = new BasicBlock("B_" + irRoot.getBBCountAndIncrease(), curFunc);
        }*/
        generateIR(node.getReExpr());
        inSideEffect = oldInSideEffect;

        //We will optimize it later
        if (curFunc.getReturnStmtNum() >= 1) {
            //be careful!
            //curBB.append(new Move(curBB, curReturnReg, node.getReExpr().regOrImm));

            assignNonMemop(curReturnReg, node.getReExpr().regOrImm, node.getReExpr());
            curBB.LinkNextBB(curReturnBB);

            if (curReturnBB.predecessorBBMap.size() == 1){
                curBB.setAdjacentBB(curReturnBB);
                curBB.finish();
            }
            else {
                curBB.finish(new Jump(curBB, curReturnBB.getName()));
            }

        }
        //curReturnBB.append(new Return(curBB, c));
    }

    @Override
    public void visit(IfStmtNode node) {
        BasicBlock BBTrue = new BasicBlock("B_ifTrue_" + irRoot.getBBCountAndIncrease(),curFunc);
        BasicBlock BBFalse = (node.getOtherwise() != null)? new BasicBlock("B_ifFalse_" + irRoot.getBBCountAndIncrease(), curFunc):null;
        BasicBlock BBMerge = new BasicBlock("B_merge_"+ irRoot.getBBCountAndIncrease(), curFunc);

        node.getCond().ifTrue = BBTrue;
        if (node.getOtherwise() == null) node.getCond().ifFalse = BBMerge;
        else node.getCond().ifFalse = BBFalse;

        //the ifTrue and ifFalse should not generate new BB
        //set SideEffect
        boolean oldInSideEffect = inSideEffect;
        inSideEffect = true;
        generateIR(node.getCond());
        inSideEffect = oldInSideEffect;

        //finish BBTrue
        curBB = BBTrue;
        generateIR(node.getThen());
        //there may be a lot of divergences
        if (!curBB.isFinish()) {
            curBB.finish(new Jump(curBB, BBMerge.getName()));
            curBB.LinkNextBB(BBMerge);
        }
        //finish BBFalse
        if (BBFalse != null) {
            curBB = BBFalse;
            generateIR(node.getOtherwise());
            if (!curBB.isFinish()) {
                curBB.finish(new Jump(curBB, BBMerge.getName()));
                curBB.LinkNextBB(BBMerge);
            }
        }

        curBB = BBMerge;
    }

    @Override
    public void visit(ForStmtNode node) {
        generateIR(node.getBegin_varDecl());
        generateIR(node.getBegin_expr());

        BasicBlock forCond = new BasicBlock("B_cond_" + irRoot.getBBCountAndIncrease(), curFunc);
        BasicBlock forBody = new BasicBlock("B_loop_" + irRoot.getBBCountAndIncrease(),curFunc);
        BasicBlock forIter = new BasicBlock("B_iter_" + irRoot.getBBCountAndIncrease(),curFunc);
        BasicBlock forMerge = new BasicBlock("B_merge_" + irRoot.getBBCountAndIncrease(), curFunc);

        curBB.LinkNextBB(forCond);
        curBB.finish();
        curBB.setAdjacentBB(forCond);

        node.getCond().ifTrue = forBody;
        node.getCond().ifFalse = forMerge;
        curBB = forCond;
        //the ifTrue and ifFalse should not generate new BB
        //set SideEffect
        boolean oldInSideEffect = inSideEffect;
        inSideEffect = true;
        generateIR(node.getCond());
        inSideEffect = oldInSideEffect;

        BasicBlock oldBreakLoop = curBreakLoopBB;
        BasicBlock oldContinueLoop = curContinueLoopBB;
        curBreakLoopBB = forMerge;
        curContinueLoopBB = forIter;

        curBB = forBody;
        generateIR(node.getBody());
        if (!forBody.isFinish()){
            curBB.finish(new Jump(curBB, forIter.getName()));
            curBB.LinkNextBB(forIter);
        }

        curBB = forIter;
        generateIR(node.getIter());
        curBB.finish(new Jump(curBB, forCond.getName()));
        curBB.LinkNextBB(forCond);

        curBreakLoopBB = oldBreakLoop;
        curContinueLoopBB = oldContinueLoop;

        curBB = forMerge;
    }

    @Override
    public void visit(WhileStmtNode node) {
        BasicBlock whileCond = new BasicBlock("B_cond_" + irRoot.getBBCountAndIncrease(), curFunc);
        BasicBlock whileBody = new BasicBlock("B_loop_" + irRoot.getBBCountAndIncrease(), curFunc);
        BasicBlock whileMerge = new BasicBlock("B_merge" + irRoot.getBBCountAndIncrease(), curFunc);

        curBB.LinkNextBB(whileCond);
        curBB.setAdjacentBB(whileCond);

        curBB = whileCond;
        node.getCond().ifTrue = whileBody;
        node.getCond().ifFalse = whileMerge;

        //this ifTrue and ifFalse should not generate new BB
        //set SideEffect
        boolean oldInSideEffect = inSideEffect;
        inSideEffect = true;
        generateIR(node.getCond());
        inSideEffect = oldInSideEffect;

        BasicBlock oldBreakLoop = curBreakLoopBB;
        BasicBlock oldContinueLoop = curContinueLoopBB;
        curBreakLoopBB = whileMerge;
        curContinueLoopBB = whileCond;

        curBB = whileBody;
        generateIR(node.getBody());
        if (!curBB.isFinish()){
            curBB.finish(new Jump(curBB, whileMerge.getName()));
            curBB.LinkNextBB(whileMerge);
        }

        curBreakLoopBB = oldBreakLoop;
        curContinueLoopBB = oldContinueLoop;

        curBB = whileMerge;
    }

    /*
    ========================= Other Stmt =========================
    */
    @Override
    public void visit(VarDeclStmtNode node) {
        generateIR(node.getVarDecl());
    }

    @Override
    public void visit(BlockNode node) {
        for (StmtNode stmtNode : node.getStmt()) {
            generateIR(stmtNode);
        }
    }

    @Override
    public void visit(ExprStmtNode node) {
        generateIR(node.expr);
    }

    /*
    ========================= expression leaf Node =========================
    */
    @Override
    public void visit(VariableNode node) {
        if (node.inClassVar != null){
            //replace it!
            node.inClassVar.ifTrue = node.ifTrue;
            node.inClassVar.ifFalse = node.ifFalse;
            generateIR(node.inClassVar);
            node.regOrImm = node.inClassVar.regOrImm;
            node.Base = node.inClassVar.Base;
            node.Index = node.inClassVar.Index;
            node.scale = node.inClassVar.scale;
            node.displacement = node.inClassVar.displacement;
        }
        else {
            //renew the information for convenient use
            //for an ExprNode, it must include a instruction
            node.regOrImm = node.getValueDefinition().varReg;
            //the leaf for the logical operation
            if (inLogicalGeneration(node)) {
                setShortCircuitLeaf(node);
            }
        }

    }


    @Override
    public void visit(ThisNode node) {
        //"this" will not be the leaf of logical expression
        node.regOrImm = node.getValueDefinition().varReg;
    }

    @Override
    public void visit(BoolLiteralNode node) {
        //in expression
        //the reg/imm will be generated whenever the condition
        if (node.getValue()) node.regOrImm = new IntImme(1);
        else node.regOrImm = new IntImme(0);

        if (inLogicalGeneration(node)){
            //when it is the leaf of condition
            if (node.getValue()) {
                //Todo !! be careful!!
                //curBB.finish(new Jump(curBB, node.ifTrue.getName()));
                curBB.LinkNextBB(node.ifTrue);
                curBB.setAdjacentBB(node.ifTrue);
            }
            else {
                //Todo !! be careful!!
                //curBB.finish(new Jump(curBB, node.ifFalse.getName()));
                curBB.LinkNextBB(node.ifFalse);
                curBB.setAdjacentBB(node.ifFalse);
            }
        }
    }

    @Override
    public void visit(IntLiteralNode node) {
        node.regOrImm = new IntImme(node.getValue());
    }

    @Override
    public void visit(NullLiteralNode node) {
        node.regOrImm = new IntImme(0);
    }

    @Override
    public void visit(StringLiteralNode node) {
        StaticString str = irRoot.stringMap.get(node.getValue());
        if (str == null){
            str = new StaticString(node.getValue(), irRoot.getStrCountAndIncrease());
            irRoot.stringMap.put(node.getValue(), str);
        }
        node.regOrImm = str;
    }

    /*
    ========================= Binary Operation Related Tool Function =========================
    */
    //for Binary operation
    private void logicalBinaryProcess(BinaryOpNode node){
        if (node.getOp() == BinaryOpNode.BinaryOp.LOG_OR){
            node.getLeft().ifTrue = node.ifTrue;
            node.getLeft().ifFalse = new BasicBlock("B_left_False" + irRoot.getBBCountAndIncrease(), curFunc);
            generateIR(node.getLeft());
            curBB = node.getLeft().ifFalse;
        }
        else{
            node.getLeft().ifTrue = new BasicBlock("B_left_True" + irRoot.getBBCountAndIncrease(), curFunc);
            node.getLeft().ifFalse = node.ifFalse;
            generateIR(node.getLeft());
            curBB = node.getLeft().ifTrue;
        }

        node.getRight().ifTrue = node.ifTrue;
        node.getRight().ifFalse = node.ifFalse;
        generateIR(node.getRight());
    }

    private void assignProcess(BinaryOpNode node){
        //generate right
        generateIR(node.getRight());
        //just tmp don't consider the memory operation
        //left expr must be left value
        //tmp I will do things like this
        LValueGetAddress = needMemAccess(node.getLeft());
        generateIR(node.getLeft());
        LValueGetAddress = false;

        //add memory after
        if (needMemAccess(node.getLeft())) assignMemOp(node.getRight().regOrImm, node.getLeft(),node.getRight());
        else assignNonMemop((Register) node.getLeft().regOrImm, node.getRight().regOrImm, node.getRight());
    }

    private void assignMemOp(Value source, ExprNode left, ExprNode right){
        //static, varDecl, return, Assign
        //"left" offer the address you need
        if (inLogicalGeneration(right)){
            // for short circuit
            // the source will be none
            BasicBlock merge = new BasicBlock("merge"+irRoot.getBBCountAndIncrease(), curFunc);
            right.ifTrue.append(new Store(right.ifTrue, source, left.Base, left.Index, left.scale, left.displacement));
            right.ifFalse.append(new Store(right.ifFalse, source, left.Base, left.Index, left.scale, left.displacement));

            right.ifTrue.finish(new Jump(right.ifTrue, merge.getName()));
            right.ifTrue.LinkNextBB(merge);

            right.ifFalse.finish(new Jump(right.ifFalse, merge.getName()));
            right.ifFalse.LinkNextBB(merge);

            //be careful! the currrent block has been changed!
            curBB = merge;
        }
        else {
            curBB.append(new Store(curBB, source, left.Base, left.Index, left.scale, left.displacement));
        }

    }

    private void assignNonMemop(Register dest, Value source, ExprNode right){
        if (inLogicalGeneration(right)){
            // for short circuit
            // the source will be none
            BasicBlock merge = new BasicBlock("merge"+irRoot.getBBCountAndIncrease(), curFunc);
            right.ifTrue.append(new Move(right.ifTrue, dest, new IntImme(1)));
            right.ifFalse.append(new Move(right.ifFalse, dest, new IntImme(0)));

            right.ifTrue.finish(new Jump(right.ifTrue, merge.getName()));
            right.ifTrue.LinkNextBB(merge);

            right.ifFalse.finish(new Jump(right.ifFalse, merge.getName()));
            right.ifFalse.LinkNextBB(merge);

            //be careful! the currrent block has been changed!
            curBB = merge;
        }
        else {
            curBB.append(new Move(curBB, dest, source));
        }
    }

    private void stringCompareProcess(BinaryOpNode node){
        generateIR(node.getRight());
        generateIR(node.getLeft());

        VirtualRegister destReg = new VirtualRegister("Reg_" + irRoot.getRegCountAndIncrease());
        curFunc.addFuncLocalVar(destReg);
        node.regOrImm = destReg;
        VirtualRegister tmpReg1;
        List<Value> argvs = new ArrayList<>();
        argvs.add(node.getLeft().regOrImm); argvs.add(node.getRight().regOrImm);
        //curBB.append(new Call(curBB, irRoot.buildInFunctionMap.get("__StringCmp"), argvs, destReg));
        switch (node.getOp()){
            case EQ:
                argvs.add(node.getLeft().regOrImm);
                argvs.add(node.getRight().regOrImm);
                curBB.append(new Call(curBB, irRoot.buildInFunctionMap.get("__StringEqual"), argvs, destReg));
                break;
            case LT:
                argvs.add(node.getLeft().regOrImm);
                argvs.add(node.getRight().regOrImm);
                curBB.append(new Call(curBB, irRoot.buildInFunctionMap.get("__StringLess"), argvs, destReg));
                break;
            case GT:
                argvs.add(node.getRight().regOrImm);
                argvs.add(node.getLeft().regOrImm);
                curBB.append(new Call(curBB, irRoot.buildInFunctionMap.get("__StringLess"), argvs, destReg));
                break;
            case LE:
                argvs.add(node.getLeft().regOrImm);
                argvs.add(node.getRight().regOrImm);
                tmpReg1 = new VirtualRegister("Reg_" + irRoot.getRegCountAndIncrease());
                curFunc.addFuncLocalVar(tmpReg1);
                curBB.append(new Call(curBB, irRoot.buildInFunctionMap.get("__StringLess"), argvs, tmpReg1));
                curBB.append(new Call(curBB, irRoot.buildInFunctionMap.get("__StringEqual"), argvs, destReg));
                curBB.append(new Binary(curBB, Binary.BinaryOp.BitOr, destReg, tmpReg1));
                break;
            case GE:
                argvs.add(node.getRight().regOrImm);
                argvs.add(node.getLeft().regOrImm);
                tmpReg1 = new VirtualRegister("Reg_" + irRoot.getRegCountAndIncrease());
                curFunc.addFuncLocalVar(tmpReg1);
                curBB.append(new Call(curBB, irRoot.buildInFunctionMap.get("__StringLess"), argvs, tmpReg1));
                curBB.append(new Call(curBB, irRoot.buildInFunctionMap.get("__StringEqual"), argvs, destReg));
                curBB.append(new Binary(curBB, Binary.BinaryOp.BitOr, destReg, tmpReg1));
                break;
                default:
                    throw new RuntimeException("Invalid string operation");
        }

    }

    private void intCompareProcess(BinaryOpNode node){
        //do not need special check
        generateIR(node.getRight());
        generateIR(node.getLeft());

        IntCompare.CompareOp op = null;
        switch (node.getOp()){
            case GE: op = IntCompare.CompareOp.GE; break;
            case LE: op = IntCompare.CompareOp.LE; break;
            case GT: op = IntCompare.CompareOp.GT; break;
            case LT: op = IntCompare.CompareOp.LT; break;
            case EQ: op = IntCompare.CompareOp.EQ; break;
            case NEQ: op = IntCompare.CompareOp.NE; break;
        }

        //thinking about the type of the oprend
        VirtualRegister destReg = new VirtualRegister("Reg_" + irRoot.getRegCountAndIncrease());
        curFunc.addFuncLocalVar(destReg);
        if (node.getLeft().regOrImm instanceof IntImme ){
            if (node.getRight().regOrImm instanceof IntImme){
                int l = ((IntImme) node.getLeft().regOrImm).getImmeValue();
                int r = ((IntImme) node.getRight().regOrImm).getImmeValue();
                boolean result = false;
                switch (node.getOp()){
                    case GE: result = l >= r; break;
                    case LE: result = l <= r; break;
                    case GT: result = l > r; break;
                    case LT: result = l < r; break;
                    case EQ: result = l == r; break;
                    case NEQ: result = l != r; break;
                }

                if (result) curBB.append(new Move(curBB, destReg, new IntImme(1)));
                else curBB.append(new Move(curBB, destReg, new IntImme(0)));
            }
            else {
                //the right type is a register
                curBB.append(new IntCompare(curBB, op, destReg, node.getLeft().regOrImm, node.getRight().regOrImm));
            }
        }
        else {
            curBB.append(new IntCompare(curBB, op, destReg, node.getLeft().regOrImm, node.getRight().regOrImm));
        }

        //in a expr
        node.regOrImm = destReg;

        if (inLogicalGeneration(node)){
            //the leaf of the condition
            curBB.finish(new Branch(curBB, destReg, node.ifTrue.getName(), node.ifFalse.getName()));
            curBB.LinkNextBB(node.ifTrue);
            curBB.LinkNextBB(node.ifFalse);
            curBB.setAdjacentBB(node.ifTrue);
        }
    }

    private void stringBinaryProcess(BinaryOpNode node){
        generateIR(node.getRight());
        generateIR(node.getLeft());

        switch (node.getOp()){
            case ADD:
                VirtualRegister destReg = new VirtualRegister("Reg_" + irRoot.getRegCountAndIncrease());
                curFunc.addFuncLocalVar(destReg);
                List<Value> argvs = new ArrayList<>();
                argvs.add(node.getLeft().regOrImm);
                argvs.add(node.getRight().regOrImm);
                curBB.append(new Call(curBB, irRoot.buildInFunctionMap.get("__StringConcat"),argvs, destReg));
                node.regOrImm = destReg;
                break;
                default: throw new RuntimeException("Invalid string operation");
        }
    }

    private void arithmeticProcess(BinaryOpNode node){
        //do not need special check
        generateIR(node.getRight());
        generateIR(node.getLeft());

        Binary.BinaryOp op;
        switch (node.getOp()){
            case R_SHIFT: op = Binary.BinaryOp.Shr; break;
            case L_SHIFT: op = Binary.BinaryOp.Shl; break;
            case BIT_AND: op = Binary.BinaryOp.BitAnd; break;
            case BIT_OR:  op = Binary.BinaryOp.BitOr; break;
            case XOR:     op = Binary.BinaryOp.Xor; break;
            case MOD:     op = Binary.BinaryOp.Mod; break;
            case MUL:     op = Binary.BinaryOp.Mul; break;
            case DIV:     op = Binary.BinaryOp.Div; break;
            case SUB:     op = Binary.BinaryOp.Sub; break;
            case ADD:     op = Binary.BinaryOp.Add; break;
            default:
                throw new RuntimeException("Unknown Binary operator");
        }

        VirtualRegister destReg = new VirtualRegister("Reg_" + irRoot.getRegCountAndIncrease());
        curFunc.addFuncLocalVar(destReg);
        if (node.getLeft().regOrImm instanceof IntImme ){
            if (node.getRight().regOrImm instanceof IntImme){
                int l = ((IntImme) node.getLeft().regOrImm).getImmeValue();
                int r = ((IntImme) node.getRight().regOrImm).getImmeValue();
                int result;
                switch (node.getOp()){
                    case R_SHIFT: result = l >> r; break;   //java's ">>" is arithmetical
                    case L_SHIFT: result = l << r; break;
                    case BIT_AND: result = l & r; break;
                    case BIT_OR:  result = l | r; break;
                    case XOR:     result = l ^ r; break;
                    case MOD:     result = l % r; break;
                    case MUL:     result = l * r; break;
                    case DIV:     result = l / r; break;
                    case SUB:     result = l - r; break;
                    case ADD:     result = l + r; break;
                    default:
                        throw new RuntimeException("Unknown Binary operator");
                }
                curBB.append(new Move(curBB, destReg, new IntImme(result)));
            }
            else {
                //the right type is a register
                curBB.append(new Move(curBB, destReg, node.getLeft().regOrImm));
                curBB.append(new Binary(curBB, op, destReg, node.getRight().regOrImm));
            }
        }
        else {
            curBB.append(new Move(curBB, destReg, node.getLeft().regOrImm));
            curBB.append(new Binary(curBB, op, destReg, node.getRight().regOrImm));
        }
        //in a expr
        node.regOrImm = destReg;
    }

    /*
    ========================= Binary Operation Node =========================
    */
    @Override
    public void visit(BinaryOpNode node) {
        switch (node.getOp()){
            case LOG_AND: case LOG_OR:
                if (node.ifFalse == null){
                    //do sth to generate the block
                    node.ifTrue = new BasicBlock("B_" + irRoot.getBBCountAndIncrease(), curFunc);
                    node.ifFalse = new BasicBlock("B_" + irRoot.getBBCountAndIncrease(), curFunc);
                }
                logicalBinaryProcess(node);
                //there must be merge at last!!!
                break;
            case ASSIGN:
                //set SideEffect
                boolean oldInSideEffect = inSideEffect;
                inSideEffect = true;
                assignProcess(node);
                inSideEffect = oldInSideEffect;
                break;
            case LE: case GE: case EQ: case NEQ: case LT: case GT:
                if (node.getRight().exprType.isString()) stringCompareProcess(node);
                else intCompareProcess(node);
                break;

            case ADD: case SUB: case DIV: case MUL: case MOD: case XOR: case BIT_OR: case BIT_AND: case L_SHIFT: case R_SHIFT:
                if (node.getRight().exprType.isString()) stringBinaryProcess(node);
                else arithmeticProcess(node);
                break;
                //debug
                default: throw new RuntimeException("Unknown operator");
        }
    }

    /*
    ========================= Unary Operation Related Tool Function =========================
    */
    private void prefixAddSubProcess(UnaryNode node, boolean isInc){
        generateIR(node.getBody());

        //ToDo: must be clean and generate no more code!! special check
        LValueGetAddress = true;
        generateIR(node.getBody());
        LValueGetAddress = false;

        node.regOrImm = node.getBody().regOrImm;
        if (isInc) curBB.append(new Inc(curBB, (Register) node.regOrImm));
        else curBB.append(new Dec(curBB, (Register) node.regOrImm));

        if (needMemAccess(node.getBody())){
            curBB.append(new Store(curBB, node.regOrImm, node.getBody().Base, node.getBody().Index, node.getBody().scale, node.getBody().displacement));
        }
        //do nothing because Inc/Dec has already changed the register
        //else curBB.append(new Move(curBB, dest, source));
    }

    private void suffixAddSubProcess(SuffixNode node, boolean isInc){
        generateIR(node.getBody());

        LValueGetAddress = needMemAccess(node.getBody());
        generateIR(node.getBody());
        LValueGetAddress = false;

        //save the old value
        VirtualRegister reg = new VirtualRegister("Reg_" + irRoot.getRegCountAndIncrease());
        curFunc.addFuncLocalVar(reg);
        curBB.append(new Move(curBB, reg, node.getBody().regOrImm));
        node.regOrImm = reg;

        if (isInc) curBB.append(new Inc(curBB, (Register) node.getBody().regOrImm));
        else curBB.append(new Dec(curBB, (Register) node.getBody().regOrImm));

        if (needMemAccess(node.getBody())){
            curBB.append(new Store(curBB, node.getBody().regOrImm, node.getBody().Base, node.getBody().Index, node.getBody().scale, node.getBody().displacement));
        }
        //do nothing because the Inc, Dec has already done
    }

    /*
    ========================= Unary Operation Node =========================
    */

    @Override
    public void visit(UnaryNode node) {
        VirtualRegister reg;
        boolean oldInSideEffect;
        switch (node.getOp()){
            case LOG_NOT:
                //set SideEffect
                oldInSideEffect = inSideEffect;
                inSideEffect = true;
                //must be merged!!
                if (node.ifFalse == null){
                    //do sth to generate the block
                    node.ifTrue = new BasicBlock("B_" + irRoot.getBBCountAndIncrease(), curFunc);
                    node.ifFalse = new BasicBlock("B_" + irRoot.getBBCountAndIncrease(), curFunc);
                }
                node.getBody().ifTrue = node.ifFalse;
                node.getBody().ifFalse = node.ifTrue;

                generateIR(node.getBody());

                inSideEffect = oldInSideEffect;
                break;
            case POS:
                generateIR(node.getBody());
                node.regOrImm = node.getBody().regOrImm;
                break;
            case BIT_NOT:
                generateIR(node.getBody());
                reg = new VirtualRegister("Reg_" + irRoot.getRegCountAndIncrease());
                curFunc.addFuncLocalVar(reg);
                node.regOrImm = reg;
                curBB.append(new Move(curBB, reg, node.getBody().regOrImm));
                curBB.append(new Unary(curBB, Unary.UnaryOp.BitNot, reg));
                break;
            case NEG:
                generateIR(node.getBody());
                reg = new VirtualRegister("Reg_" + irRoot.getRegCountAndIncrease());
                curFunc.addFuncLocalVar(reg);
                node.regOrImm = reg;
                curBB.append(new Move(curBB, reg, node.getBody().regOrImm));
                curBB.append(new Unary(curBB, Unary.UnaryOp.BitNot, reg));
                break;
            case INC:
                //set SideEffect
                oldInSideEffect = inSideEffect;
                inSideEffect = true;
                prefixAddSubProcess(node, true);
                inSideEffect = oldInSideEffect;
                break;
            case DEC:
                //set SideEffect
                oldInSideEffect = inSideEffect;
                inSideEffect = true;
                prefixAddSubProcess(node, false);
                inSideEffect = oldInSideEffect;
                break;

            default:
                throw new RuntimeException("Unknown Unary Prefix Op");
        }
    }


    @Override
    public void visit(SuffixNode node) {
        //set SideEffect
        boolean oldInSideEffect = inSideEffect;
        inSideEffect = true;
        //there must exist sideEffect!!
        switch (node.getOp()){
            case INC:
                suffixAddSubProcess(node, true);
                break;
            case DEC:
                suffixAddSubProcess(node, false);
                break;
                default:
                    throw new RuntimeException("Unknown Unary Suffix Op");
        }
        inSideEffect = oldInSideEffect;
    }

    /*
    ========================= Memory Access Node =========================
    */
    @Override
    public void visit(ArrayIndexNode node) {
        boolean oldLValueGetAddress = LValueGetAddress;
        LValueGetAddress = false;

        generateIR(node.getArray());
        generateIR(node.getIndex());

        LValueGetAddress = oldLValueGetAddress;

        VirtualRegister destReg = new VirtualRegister("Reg_" + irRoot.getRegCountAndIncrease());
        curFunc.addFuncLocalVar(destReg);
        if (LValueGetAddress){
            node.Base = (Register) node.getArray().regOrImm;
            if (node.getIndex().regOrImm instanceof IntImme){
                VirtualRegister ImmeReg = new VirtualRegister("Reg_" + irRoot.getRegCountAndIncrease());
                curFunc.addFuncLocalVar(ImmeReg);
                curBB.append(new Move(curBB, ImmeReg, node.getIndex().regOrImm));
                node.Index = ImmeReg;
            }
            else node.Index = (Register) node.getIndex().regOrImm;
            node.scale = 8;
            node.displacement = new IntImme(intSize); // it is the size of int!!
        }
        else {
            if (node.getIndex().regOrImm instanceof IntImme){
                VirtualRegister ImmeReg = new VirtualRegister("Reg_" + irRoot.getRegCountAndIncrease());
                curFunc.addFuncLocalVar(ImmeReg);
                curBB.append(new Move(curBB, ImmeReg, node.getIndex().regOrImm));
                //the scale is set for 8!!
                //the first element is set to store size!!
                curBB.append(new Load(curBB, destReg, (Register) node.getArray().regOrImm, ImmeReg, 8, new IntImme(intSize)));
            }
            else curBB.append(new Load(curBB,destReg, (Register) node.getArray().regOrImm, (Register) node.getIndex().regOrImm, 8, new IntImme(intSize)));
            node.regOrImm = destReg;
        }

        //The leaf of the short circuit generation
        if (inLogicalGeneration(node)){
            setShortCircuitLeaf(node);
        }
    }

    @Override
    public void visit(MemberAccessNode node) {
        //The leaf of the short circuit generation

        boolean oldLValueGetAddress = LValueGetAddress;
        LValueGetAddress = false;
        generateIR(node.getObject());
        LValueGetAddress = oldLValueGetAddress;
        ClassDeclNode belongClass = node.getObject().getExprType().getTypeDefinition();
        if (LValueGetAddress){
            node.Base = (Register) node.getObject().regOrImm;
            node.displacement = new IntImme(belongClass.MemOffset.get(node.getMemberRef())); // it is the size of int!!
        }
        else {
            VirtualRegister destReg = new VirtualRegister("Reg_" + irRoot.getRegCountAndIncrease());
            curFunc.addFuncLocalVar(destReg);
            curBB.append(new Load(curBB, destReg, (Register) node.getObject().regOrImm, null, 0, new IntImme(belongClass.MemOffset.get(node.getMemberRef()))));
            node.regOrImm = destReg;
        }

        if (inLogicalGeneration(node)){
            setShortCircuitLeaf(node);
        }
    }

    /*
    ========================= New-operation-related tool function =========================
    */
    private void generateNewDim(int curDim, List<ExprNode> arrayDim, VirtualRegister AddressBase){
        //leftDim: how many dims left for me to generate new loop
        //arrayDim: the expr for the dim
        //allDim: the dim of the new node
        if (curDim >= arrayDim.size()) return;

        VirtualRegister itrReg = new VirtualRegister("Reg_" + irRoot.getRegCountAndIncrease());
        curFunc.addFuncLocalVar(itrReg);
        curBB.append(new Move(curBB, itrReg, new IntImme(0)));

        BasicBlock Cond = new BasicBlock("B_cond_" + irRoot.getBBCountAndIncrease(), curFunc);
        BasicBlock Body = new BasicBlock("B_loop_" + irRoot.getBBCountAndIncrease(), curFunc);
        BasicBlock Merge = new BasicBlock("B_merge" + irRoot.getBBCountAndIncrease(), curFunc);

        curBB.LinkNextBB(Cond);
        curBB.setAdjacentBB(Cond);

        curBB = Cond;
        VirtualRegister cmpReg = new VirtualRegister("Reg_" + irRoot.getRegCountAndIncrease());
        curFunc.addFuncLocalVar(cmpReg);
        curBB.append(new IntCompare(curBB, IntCompare.CompareOp.LE, cmpReg, itrReg, arrayDim.get(curDim - 1).regOrImm));
        curBB.finish(new Branch(curBB, cmpReg, Body.getName(), Merge.getName()));
        curBB.LinkNextBB(Body);
        curBB.LinkNextBB(Merge);
        curBB.setAdjacentBB(Body);

        //get the current dim
        ExprNode curExprDim = arrayDim.get(curDim);
        generateIR(curExprDim);

        //link the Body!!
        curBB = Body;
        VirtualRegister tmpAlloc = new VirtualRegister("Reg_" + irRoot.getRegCountAndIncrease());
        curFunc.addFuncLocalVar(tmpAlloc);
        curBB.append(new Move(curBB, tmpAlloc, new IntImme(8)/*the size of build in type or pointer*/));
        curBB.append(new Binary(curBB, Binary.BinaryOp.Mul, tmpAlloc, curExprDim.regOrImm));
        curBB.append(new Binary(curBB, Binary.BinaryOp.Add, tmpAlloc, new IntImme(intSize)));
        curBB.append(new HeapAllocate(curBB, tmpAlloc, tmpAlloc));
        curBB.append(new Store(curBB, curExprDim.regOrImm, tmpAlloc, null, 0, null));
        //save the tmp alloc address to memory
        curBB.append(new Store(curBB, tmpAlloc, AddressBase, itrReg,8, new IntImme(intSize)));

        //recursively generate the code
        generateNewDim(curDim + 1, arrayDim, tmpAlloc);

        //be careful! now the curBB may be changed!
        curBB.append(new Inc(curBB, itrReg));
        curBB.finish(new Jump(curBB, Cond.getName()));
        curBB.LinkNextBB(Cond);

        curBB = Merge;
    }

    /*
    ========================= New Node =========================
    */
    @Override
    public void visit(NewNode node) {
        Type type = node.getExprType();
        if (!type.isBuildInType() && !type.isArray()) {
            //user-defined class
            //Todo : may call the constructor!!
            VirtualRegister reg = new VirtualRegister("Reg_" + irRoot.getRegCountAndIncrease());
            curFunc.addFuncLocalVar(reg);
            curBB.append(new HeapAllocate(curBB, reg, new IntImme(type.getTypeDefinition().getAllocSize())));
            node.regOrImm = reg;
            if (node.getExprType().getTypeDefinition().getConstructMethod() != null){
                Function constructor = node.getExprType().getTypeDefinition().getConstructMethod().irFunction;
                //call constructor, with no return value!!
                List<Value> argvs = new ArrayList<>();
                //use the reg of myself as the parameter("this")
                argvs.add(reg);
                curBB.append(new Call(curBB, constructor, argvs, null));
            }
        }
        else{
            //array
            ExprNode firstDim = node.getExprDim().get(0);
            generateIR(firstDim);
            VirtualRegister reg = new VirtualRegister("Reg_" + irRoot.getRegCountAndIncrease());
            curFunc.addFuncLocalVar(reg);
            node.regOrImm = reg;
            curBB.append(new Move(curBB, reg, new IntImme(node.getExprType().getRegisterSize())));
            curBB.append(new Binary(curBB, Binary.BinaryOp.Mul, reg, firstDim.regOrImm));
            curBB.append(new Binary(curBB, Binary.BinaryOp.Add, reg, new IntImme(intSize)));
            curBB.append(new HeapAllocate(curBB, reg, reg));
            curBB.append(new Store(curBB, firstDim.regOrImm, reg, null, 0, null));

            int curDim = 1;
            generateNewDim(curDim, node.getExprDim(), reg);
        }
    }

    /*
    ========================= Method-Access-related tool function =========================
    */
    // with optimization
    private void processPrint(ExprNode node, boolean isNewLine){
        if (node instanceof BinaryOpNode){
            //print(A + B)
            BinaryOpNode tmpNode = (BinaryOpNode) node;
            if (tmpNode.getOp() == BinaryOpNode.BinaryOp.ADD){
                processPrint(tmpNode.getLeft(), false);
                processPrint(tmpNode.getRight(), isNewLine);
                return;
            }
            //terminal
            else throw new RuntimeException("Error operator in \"print\"");
        }
        else if (node instanceof CallNode){
            //print(toString(a))
            //to avoid the same name "toString" conflict
            FuncDeclNode funcNode = ((CallNode) node).getFuncDefinition();
            if (funcNode.getName().equals("toString") && funcNode.isBuildIn){
                ExprNode num = ((CallNode) node).getFuncParams().get(0);
                generateIR(num);
                List<Value> argvs = new ArrayList<>();
                argvs.add(num.regOrImm);
                if (isNewLine){
                    curBB.append(new Call(curBB, irRoot.buildInFunctionMap.get("__PrintlnInt"), argvs, null));
                }
                else {
                    curBB.append(new Call(curBB, irRoot.buildInFunctionMap.get("__PrintInt"), argvs, null));
                }
                return;
            }
        }

        //print(string)
        generateIR(node);
        List<Value> argvs = new ArrayList<>();
        argvs.add(node.regOrImm);
        if (isNewLine){
            curBB.append(new Call(curBB, irRoot.buildInFunctionMap.get("__PrintlnString"), argvs, null));
        }
        else {
            curBB.append(new Call(curBB, irRoot.buildInFunctionMap.get("__PrintString"), argvs, null));
        }
        return;
    }

    private void processBuildInFunc(CallNode node){
        //we do not need backup here
        String funcName = node.getFuncName();
        if (funcName.equals("print") || funcName.equals("println")){
            processPrint(node.getFuncParams().get(0), funcName.equals("println"));
        }
        else if (funcName.equals("getString")){
            VirtualRegister destReg = new VirtualRegister("Reg_" + irRoot.getRegCountAndIncrease());
            curFunc.addFuncLocalVar(destReg);
            curBB.append(new Call(curBB, irRoot.buildInFunctionMap.get("__GetString"), new ArrayList<>(), destReg));
            node.regOrImm = destReg;
        }
        else if (funcName.equals("getInt")){
            VirtualRegister destReg = new VirtualRegister("Reg_" + irRoot.getRegCountAndIncrease());
            curFunc.addFuncLocalVar(destReg);
            curBB.append(new Call(curBB, irRoot.buildInFunctionMap.get("__GetInt"), new ArrayList<>(), destReg));
            node.regOrImm = destReg;
        }
        else if (funcName.equals("toString")){
            generateIR(node.getFuncParams().get(0));
            VirtualRegister destReg = new VirtualRegister("Reg_" + irRoot.getRegCountAndIncrease());
            curFunc.addFuncLocalVar(destReg);
            List<Value> argvs = new ArrayList<>();
            argvs.add(node.getFuncParams().get(0).regOrImm);
            curBB.append(new Call(curBB, irRoot.buildInFunctionMap.get("__ToString"), argvs, destReg));
            node.regOrImm = destReg;
        }
        else throw new RuntimeException("Undefined build-in Function");
    }

    private void processBuildInClassMethod(MethodAccessNode node){
        //we may need backup here
        boolean oldLValueGetAddress = LValueGetAddress;
        String method = node.getMethodName();

        //visit the parameters
        node.getMethodParams().forEach(x -> generateIR(x));
        List<Value> argvs = new ArrayList<>();
        node.getMethodParams().forEach(x -> argvs.add(x.regOrImm));

        //add "this"
        argvs.add(node.getObject().regOrImm);

        if (method.equals("size")){
            //array.size || string.length
            //Todo double check this
            VirtualRegister destReg = new VirtualRegister("Reg_size_" + irRoot.getRegCountAndIncrease());
            curFunc.addFuncLocalVar(destReg);
            curBB.append(new Load(curBB, destReg, (Register) node.getObject().regOrImm, null, 0, null));
            node.regOrImm = destReg;
        }
        else if (method.equals("length")){
            /*
            VirtualRegister destReg = new VirtualRegister("Reg_length_" + irRoot.getRegCountAndIncrease());
            curFunc.addFuncLocalVar(destReg);
            curBB.append(new Load(curBB, destReg, (Register) node.getObject().regOrImm, null, 0, new IntImme(- 8)));
            node.regOrImm = destReg;
            special attention
            */
        }
        else if (method.equals("substring")){
            VirtualRegister destReg = new VirtualRegister("Reg_substr_" + irRoot.getRegCountAndIncrease());
            curFunc.addFuncLocalVar(destReg);
            curBB.append(new Call(curBB, irRoot.buildInFunctionMap.get("__StringSubString"), argvs, destReg));
            node.regOrImm = destReg;
        }
        else if (method.equals("parseInt")){
            VirtualRegister destReg = new VirtualRegister("Reg_parInt_" + irRoot.getRegCountAndIncrease());
            curFunc.addFuncLocalVar(destReg);
            curBB.append(new Call(curBB, irRoot.buildInFunctionMap.get("__StringParseInt"), argvs, destReg));
            node.regOrImm = destReg;
        }
        else if (method.equals("ord")){
            VirtualRegister destReg = new VirtualRegister("Reg_ord_" + irRoot.getRegCountAndIncrease());
            curFunc.addFuncLocalVar(destReg);
            curBB.append(new Call(curBB, irRoot.buildInFunctionMap.get("__StringOrd"), argvs, destReg));
            node.regOrImm = destReg;
        }
        else throw new RuntimeException("Undefined build-in class method");

        LValueGetAddress = oldLValueGetAddress;
    }


    /*
    ========================= Method Access Node =========================
    */
    @Override
    public void visit(CallNode node) {
        boolean oldInSideEffect = inSideEffect;
        inSideEffect = true;
        if (node.getFuncDefinition().isBuildIn){
            processBuildInFunc(node);
        }
        else {
            //lambda expr
            node.getFuncParams().forEach(x -> generateIR(x));
            List<Value> argvs = new ArrayList<>();
            node.getFuncParams().forEach(x -> argvs.add(x.regOrImm));
            if (!node.getFuncDefinition().getFuncReturnType().isNull()) {
                VirtualRegister destReg = new VirtualRegister("Reg_" + irRoot.getRegCountAndIncrease());
                curFunc.addFuncLocalVar(destReg);
                curBB.append(new Call(curBB, node.getFuncDefinition().irFunction, argvs, destReg));
                node.regOrImm = destReg;
            }
            else{
                curBB.append(new Call(curBB, node.getFuncDefinition().irFunction, argvs,null));
            }
        }

        inSideEffect = oldInSideEffect;
        if (inLogicalGeneration(node)) {
            setShortCircuitLeaf(node);
        }
    }

    @Override
    public void visit(MethodAccessNode node) {
        //set SideEffect
        boolean oldInSideEffect = inSideEffect;
        inSideEffect = true;
        //be carefull!
        generateIR(node.getObject());

        if (node.getFuncDefinition().isBuildIn){
            processBuildInClassMethod(node);
        }
        else{
            node.getMethodParams().forEach(x -> generateIR(x));
            List<Value> argvs = new ArrayList<>();
            node.getMethodParams().forEach(x -> argvs.add(x.regOrImm));
            //add "this" parameter
            argvs.add(node.getObject().regOrImm);
            if (!node.getFuncDefinition().getFuncReturnType().isNull()) {
                VirtualRegister destReg = new VirtualRegister("Reg_" + irRoot.getRegCountAndIncrease());
                curFunc.addFuncLocalVar(destReg);
                curBB.append(new Call(curBB, node.getFuncDefinition().irFunction, argvs, destReg));
                node.regOrImm = destReg;
            }
            else{
                curBB.append(new Call(curBB, node.getFuncDefinition().irFunction, argvs,null));
            }
        }

        //The leaf of the short circuit generation
        if (inLogicalGeneration(node)){
            setShortCircuitLeaf(node);
        }
        inSideEffect = oldInSideEffect;
    }
}
