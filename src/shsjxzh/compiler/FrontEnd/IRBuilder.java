package shsjxzh.compiler.FrontEnd;

import shsjxzh.compiler.AST.ASTNode;
import shsjxzh.compiler.AST.ASTVisitor;
import shsjxzh.compiler.AST.Decl.ClassDeclNode;
import shsjxzh.compiler.AST.Decl.DeclNode;
import shsjxzh.compiler.AST.Decl.FuncDeclNode;
import shsjxzh.compiler.AST.Decl.VarDeclNode;
import shsjxzh.compiler.AST.Expr.*;
import shsjxzh.compiler.AST.ProgramNode;
import shsjxzh.compiler.AST.Stmt.*;
import shsjxzh.compiler.IR.BasicBlock;
import shsjxzh.compiler.IR.Function;
import shsjxzh.compiler.IR.IRRoot;
import shsjxzh.compiler.IR.Instruction.*;
import shsjxzh.compiler.IR.Value.*;

import java.util.LinkedList;

public class IRBuilder implements ASTVisitor {
    BasicBlock curBB;
    Function curFunc;

    BasicBlock curReturnBB;
    Register curReturnReg;

    BasicBlock curBreakLoopBB;
    BasicBlock curContinueLoopBB;


    boolean isFuncParam = false;

    boolean oldInSideEffect;
    boolean inSideEffect = false;
    
    boolean oldLValueGetAddress;
    boolean LValueGetAddress = false;

    private IRRoot irRoot = new IRRoot();

    public IRRoot getIrRoot() {
        return irRoot;
    }

    private void setSideEffect() {
        oldInSideEffect = inSideEffect;
        inSideEffect = true;
    }

    private void exitSideEffect() {
        inSideEffect = oldInSideEffect;
    }

    private void setShortCircuitLeaf(ExprNode node){
        curBB.finish(new Branch(curBB, node.regOrImm, node.ifTrue.getName(),node.ifFalse.getName()));
        curBB.LinkNextBB(node.ifTrue);
        curBB.LinkNextBB(node.ifFalse);
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

    private boolean isLogicalExpr(ExprNode node){
        if (node instanceof BinaryOpNode){
            BinaryOpNode.BinaryOp op = ((BinaryOpNode) node).getOp();
            if (op == BinaryOpNode.BinaryOp.LOG_OR || op == BinaryOpNode.BinaryOp.LOG_AND){
                return true;
            }
        }
        if (node instanceof UnaryNode){
            UnaryNode.UnaryOp op = ((UnaryNode) node).getOp();
            if (op == UnaryNode.UnaryOp.LOG_NOT){
                return true;
            }
        }
        return false;
    }

    private boolean needMemAccess(ExprNode node){
        if (node instanceof ArrayIndexNode || node instanceof MemberAccessNode) return true;
        else return false;
    }

    /*
    void generateExprIR(ExprNode node){
        //for all the expr node, if it is not the special case(like in if, for, while), then it must be special judge
        if (node != null) {
            if (inSideEffect || node.hasSideEffect) {
                //set the generation
                if (node.hasSideEffect) inSideEffect = true;

                if (isLogicalExpr(node) && node.ifTrue == null) {
                    node.ifTrue = new BasicBlock("B_" + irRoot.getBBCountAndIncrease(), curFunc);
                    node.ifFalse = new BasicBlock("B_" + irRoot.getBBCountAndIncrease(), curFunc);
                }
                node.accept(this);
                inSideEffect = false;

            }
        }
    }*/

    void staticVarGenerate(VarDeclNode node){
        setSideEffect();

        StaticData data = new StaticSpace(node.getName(), node.getVarType().getRegisterSize());
        irRoot.staticDataList.add(data);

        VirtualRegister reg = new VirtualRegister("Reg_" + node.getName());
        irRoot.getRegCountAndIncrease();
        node.varReg = reg;

        if (node.getExpr() != null){
            //check this!!
            generateIR(node.getExpr());
            //tmp operation
            //change when Memory operation is needed
            //curBB.append(new Move(curBB, reg, node.getExpr().regOrImm));
            assignNonMemop(reg, node.getExpr().regOrImm, node.getExpr());
        }
        else{
            curBB.append(new Move(curBB, reg, new IntImme(0)));
        }

        exitSideEffect();
    }

    @Override
    public void visit(ProgramNode node) {
        /*  ToDo: all the nodes that may be the leaf of condition must be carefully handled,
            list:
            arrayIndex, compare op, bool const, call(func & class method), member access, unary op("!")
        */
        //  If you want to cut memory use in compiling, change jump (label) to jump (pointer)

        curFunc = irRoot.functionMap.get("__init");
        curBB = curFunc.getStartBB();
        for (DeclNode declNode : node.getDeclnodes()) {
            if (declNode instanceof VarDeclNode){
                staticVarGenerate( (VarDeclNode) declNode);
            }
        }
        generateIR(node.getMainDecl());

        //finish the generation of "__init" function
        //pretend that there will be no parameters for "main"
        curFunc = irRoot.functionMap.get("__init");
        curBB = curFunc.getStartBB();
        VirtualRegister reg = new VirtualRegister("Reg_" + irRoot.getRegCountAndIncrease());
        curBB.append(new Call(curBB, irRoot.functionMap.get("main"), new LinkedList<>(), reg));
        //return node will point to a return basic block
        //but there is no need for a init function
        curBB.finish(new Return(curBB, reg));

        for (DeclNode declNode : node.getDeclnodes()) {
            if (!declNode.isBuildIn) {
                //Todo: handle the build-in function and class
                generateIR(declNode);
            }
        }
    }

    @Override
    public void visit(FuncDeclNode node) {
        curFunc = new Function(node.getName(), "B_" + irRoot.getBBCountAndIncrease(), node.returnNum, node.getFuncReturnType().getRegisterSize());
        node.irFunction = curFunc;
        //already put!!
        irRoot.functionMap.put(curFunc.getName(), curFunc);
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
        //ToDo
    }

    @Override
    public void visit(VarDeclNode node) {
        //local variable
        setSideEffect();

        VirtualRegister reg = new VirtualRegister("Reg_" + node.getName());
        irRoot.getRegCountAndIncrease();
        node.varReg = reg;

        if (node.getExpr() != null){
            generateIR(node.getExpr());
            //tmp operation
            assignNonMemop(reg, node.getExpr().regOrImm, node.getExpr());
        }
        else if (isFuncParam){
            curFunc.funcParams.add(reg);
        }
        else{
            curBB.append(new Move(curBB, reg, new IntImme(0)));
        }

        exitSideEffect();
    }

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
    public void visit(ExprStmtNode node) {
        generateIR(node.expr);
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
        curBB.setAdjacentBB(forCond);

        node.getCond().ifTrue = forBody;
        node.getCond().ifFalse = forMerge;
        curBB = forCond;
        //the ifTrue and ifFalse should not generate new BB
        generateIR(node.getCond());

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
    public void visit(IfStmtNode node) {
        BasicBlock BBTrue = new BasicBlock("B_ifTrue_" + irRoot.getBBCountAndIncrease(),curFunc);
        BasicBlock BBFalse = (node.getOtherwise() != null)? new BasicBlock("B_ifFalse_" + irRoot.getBBCountAndIncrease(), curFunc):null;
        BasicBlock BBMerge = new BasicBlock("B_merge_"+ irRoot.getBBCountAndIncrease(), curFunc);

        node.getCond().ifTrue = BBTrue;
        if (node.getOtherwise() == null) node.getCond().ifFalse = BBMerge;
        else node.getCond().ifFalse = BBFalse;

        //the ifTrue and ifFalse should not generate new BB
        generateIR(node.getCond());

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
    public void visit(ReturnStmtNode node) {
        //there will be a little more thing to do
        //simple optimization for "return"
        //be careful!
        setSideEffect();

        /*if (isLogicalExpr(node.getReExpr())) {
            node.getReExpr().ifTrue = new BasicBlock("B_" + irRoot.getBBCountAndIncrease(), curFunc);
            node.getReExpr().ifFalse = new BasicBlock("B_" + irRoot.getBBCountAndIncrease(), curFunc);
        }*/

        generateIR(node.getReExpr());

        //We will optimize it later
        if (curFunc.getReturnStmtNum() >= 1) {
            //be careful!
            //curBB.append(new Move(curBB, curReturnReg, node.getReExpr().regOrImm));

            assignNonMemop(curReturnReg, node.getReExpr().regOrImm, node.getReExpr());

            curBB.finish(new Jump(curBB, curReturnBB.getName()));
            curBB.LinkNextBB(curReturnBB);
        }
        //curReturnBB.append(new Return(curBB, c));

        exitSideEffect();
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
        generateIR(node.getCond());

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

    @Override
    public void visit(BoolLiteralNode node) {
        //in expression
        //the reg/imm will be generated whenever the condition
        if (node.getValue()) node.regOrImm = new IntImme(1);
        else node.regOrImm = new IntImme(0);

        if (inLogicalGeneration(node)){
            //when it is the leaf of condition
            if (node.getValue()) {
                curBB.finish(new Jump(curBB, node.ifTrue.getName()));
                curBB.LinkNextBB(node.ifTrue);
            }
            else {
                curBB.finish(new Jump(curBB, node.ifFalse.getName()));
                curBB.LinkNextBB(node.ifFalse);
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
        // ToDo
    }

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
                setSideEffect();
                assignProcess(node);
                exitSideEffect();
                break;
            case LE: case GE: case EQ: case NEQ: case LT: case GT:
                if (node.getRight().exprType.isString()) stringCompareProcess(node);
                else intCompareProcess(node);

            case ADD: case SUB: case DIV: case MUL: case MOD: case XOR: case BIT_OR: case BIT_AND: case L_SHIFT: case R_SHIFT:
                arithmeticProcess(node);
                break;
                //debug
                default: throw new RuntimeException("Unknown operator");
        }
    }
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
        //ToDo
        //generate right
        generateIR(node.getRight());
        //just tmp don't consider the memory operation
        //left expr must be left value
        LValueGetAddress = true;
        generateIR(node.getLeft());
        LValueGetAddress = false;

        //add memory after
        if (needMemAccess(node.getLeft())) assignMemOp();
        else assignNonMemop((Register) node.getLeft().regOrImm, node.getRight().regOrImm, node.getRight());
    }

    private void assignMemOp(){
        //static, varDecl, return, Assign

        ;
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
        //ToDo
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

        VirtualRegister reg = new VirtualRegister("Reg_" + irRoot.getRegCountAndIncrease());
        curBB.append(new IntCompare(curBB, op, reg, node.getLeft().regOrImm, node.getRight().regOrImm));
        //in a expr
        node.regOrImm = reg;

        if (inLogicalGeneration(node)){
            //the leaf of the condition
            curBB.finish(new Branch(curBB, reg, node.ifTrue.getName(), node.ifFalse.getName()));
            curBB.LinkNextBB(node.ifTrue);
            curBB.LinkNextBB(node.ifFalse);
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
                throw new RuntimeException("Unknown operator");
        }
        VirtualRegister reg = new VirtualRegister("Reg_"+ irRoot.getRegCountAndIncrease());
        node.regOrImm = reg;
        curBB.append(new Binary(curBB, op, reg, node.getLeft().regOrImm, node.getRight().regOrImm));
    }

    @Override
    public void visit(UnaryNode node) {
        //setSideEffect
    }

    @Override
    public void visit(SuffixNode node) {
        //setSideEffect
    }

    @Override
    public void visit(ArrayIndexNode node) {
        oldLValueGetAddress = LValueGetAddress;
        LValueGetAddress = false;

        generateIR(node.getIndex());
        generateIR(node.getArray());

        LValueGetAddress = oldLValueGetAddress;

        VirtualRegister reg = new VirtualRegister("Reg_" + irRoot.getRegCountAndIncrease());
        if (LValueGetAddress){

        }
        else {
            curBB.append(new Load(curBB,reg, ));
        }

        //The leaf of the short circuit generation
        if (inLogicalGeneration(node)){
            setShortCircuitLeaf(node);
        }

    }

    @Override
    public void visit(VariableNode node) {
        //renew the information for convenient use
        //for an ExprNode, it must include a instruction
        node.regOrImm = node.getValueDefinition().varReg;
        //the leaf for the logical operation
        if (inLogicalGeneration(node)){
            setShortCircuitLeaf(node);
        }
    }

    @Override
    public void visit(CallNode node) {
        setSideEffect();
        //It is the leaf of the bool comparison, be careful!


        //The leaf of the short circuit generation
        if (inLogicalGeneration(node)){
            setShortCircuitLeaf(node);
        }
        exitSideEffect();
    }

    @Override
    public void visit(MemberAccessNode node) {

        //The leaf of the short circuit generation
        if (inLogicalGeneration(node)){
            setShortCircuitLeaf(node);
        }
    }

    @Override
    public void visit(MethodAccessNode node) {
        setSideEffect();


        //The leaf of the short circuit generation
        if (inLogicalGeneration(node)){
            setShortCircuitLeaf(node);
        }
        exitSideEffect();
    }

    @Override
    public void visit(NewNode node) {

    }

    @Override
    public void visit(ThisNode node) {

    }
}
