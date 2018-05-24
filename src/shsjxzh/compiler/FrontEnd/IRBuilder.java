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
    boolean isFuncParam = false;
    private IRRoot irRoot = new IRRoot();

    public IRRoot getIrRoot() {
        return irRoot;
    }

    void generateIR(ASTNode node){
        if (node != null){
            node.accept(this);
        }
    }

    void staticVarGenerate(VarDeclNode node){
        StaticData data = new StaticSpace(node.getName(), node.getVarType().getRegisterSize());
        irRoot.staticDataList.add(data);

        VirtualRegister reg = new VirtualRegister("Reg_" + node.getName());
        irRoot.getRegCountAndIncrease();
        node.varReg = reg;

        if (node.getExpr() != null){
            generateIR(node.getExpr());
            //tmp operation
            //change when Memory operation is needed
            curBB.append(new Move(curBB, reg, node.getExpr().regOrImm));
        }
        else{
            curBB.append(new Move(curBB, reg, new IntImme(0)));
        }
    }

    @Override
    public void visit(ProgramNode node) {
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
                generateIR(declNode);
            }
        }
    }

    @Override
    public void visit(FuncDeclNode node) {
        curFunc = new Function(node.getName(), "B_" + irRoot.getBBCountAndIncrease());
        node.irFunction = curFunc;
        //already put!!
        irRoot.functionMap.put(curFunc.getName(), curFunc);
        curBB = curFunc.getStartBB();
        curFunc.setReturnSize(node.getFuncReturnType().getRegisterSize());

        isFuncParam = true;
        for (VarDeclNode varDeclNode : node.getFuncParams()) {
            //special handle
            generateIR(varDeclNode);
        }
        isFuncParam = false;

        curReturnBB = new BasicBlock("B_" + irRoot.getBBCountAndIncrease(), curFunc);
        curReturnReg = new VirtualRegister("Reg_" + irRoot.getRegCountAndIncrease());
        generateIR(node.getFuncBlock());
        curReturnBB.append(new Return(curReturnBB, curReturnReg));
        //thinking about multiple return
    }

    @Override
    public void visit(ClassDeclNode node) {
        //tmp do nothing
    }

    @Override
    public void visit(VarDeclNode node) {
        //local variabel
        VirtualRegister reg = new VirtualRegister("Reg_" + node.getName());
        irRoot.getRegCountAndIncrease();
        node.varReg = reg;

        if (node.getExpr() != null){
            generateIR(node.getExpr());
            //tmp operation
            curBB.append(new Move(curBB, reg, node.getExpr().regOrImm));
        }
        else if (isFuncParam){
            curFunc.funcParams.add(reg);
        }
        else{
            curBB.append(new Move(curBB, reg, new IntImme(0)));
        }
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

    }

    @Override
    public void visit(ContinueStmtNode node) {

    }

    @Override
    public void visit(ExprStmtNode node) {
        generateIR(node.expr);
    }

    @Override
    public void visit(ForStmtNode node) {

    }

    @Override
    public void visit(IfStmtNode node) {
        BasicBlock BBTrue = new BasicBlock("B_if_true" + irRoot.getBBCountAndIncrease(),curFunc);
        BasicBlock BBFalse = (node.getOtherwise() != null)? new BasicBlock("B_if_false" + irRoot.getBBCountAndIncrease(), curFunc):null;
        BasicBlock BBMerge = new BasicBlock("B_if_merge"+ irRoot.getBBCountAndIncrease(), curFunc);

        node.getCond().ifTrue = BBTrue;
        if (node.getOtherwise() == null) node.getCond().ifFalse = BBMerge;
        else node.getCond().ifFalse = BBFalse;

        generateIR(node.getCond());

        //finish BBTrue
        curBB = BBTrue;
        generateIR(node.getThen());
        //there may be a lot of divergences
        if (!curBB.isFinish()) {
            curBB.finish(new Jump(curBB, BBMerge));
        }
        //finish BBFalse
        if (BBFalse != null) {
            curBB = BBFalse;
            generateIR(node.getOtherwise());
            if (!curBB.isFinish()) {
                curBB.finish(new Jump(curBB, BBMerge));
            }
        }

        //will the above handle it well??
        curBB = BBMerge;
    }

    @Override
    public void visit(ReturnStmtNode node) {
        generateIR(node.getReExpr());
        curBB.append(new Move(curBB,curReturnReg,node.getReExpr().regOrImm));
        curBB.finish(new Jump(curBB,curReturnBB));
        //curReturnBB.append(new Return(curBB, c));
    }

    @Override
    public void visit(WhileStmtNode node) {
        BasicBlock whileBody = new BasicBlock("B_whileBody_" + irRoot.getBBCountAndIncrease(),curFunc);
    }

    @Override
    public void visit(BoolLiteralNode node) {
        if (node.getValue()) node.regOrImm = new IntImme(1);
        else node.regOrImm = new IntImme(0);
        //when in condition node
        //does it be right at all condition?
        if (node.ifTrue != null){
            if (node.getValue()) {
                curBB.finish(new Jump(curBB, node.ifTrue));
            }
            else {
                curBB.finish(new Jump(curBB, node.ifFalse));
            }
        }
    }

    @Override
    public void visit(IntLiteralNode node) {
        node.regOrImm = new IntImme(node.getValue());
    }

    @Override
    public void visit(NullLiteralNode node) {

    }

    @Override
    public void visit(StringLiteralNode node) {

    }

    @Override
    public void visit(BinaryOpNode node) {
        switch (node.getOp()){
            case LOG_AND: case LOG_OR:
                logicalBinaryProcess(node);
                break;
                default: break;
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

    @Override
    public void visit(UnaryNode node) {

    }

    @Override
    public void visit(SuffixNode node) {

    }

    @Override
    public void visit(ArrayIndexNode node) {

    }

    @Override
    public void visit(VariableNode node) {
        //renew the information for convenient use
        //for an ExprNode, it must include a
        node.regOrImm = node.getValueDefinition().varReg;
        //the leaf for the logical operation
        if (node.ifTrue != null){
            curBB.finish(new Branch(curBB, node.regOrImm, node.ifTrue,node.ifFalse));
        }
    }

    @Override
    public void visit(CallNode node) {

    }

    @Override
    public void visit(MemberAccessNode node) {

    }

    @Override
    public void visit(MethodAccessNode node) {

    }

    @Override
    public void visit(NewNode node) {

    }

    @Override
    public void visit(ThisNode node) {

    }
}
