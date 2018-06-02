package shsjxzh.compiler.BackEnd;

import shsjxzh.compiler.IR.*;
import shsjxzh.compiler.IR.Instruction.*;
import shsjxzh.compiler.IR.Value.*;

import java.io.PrintStream;
import java.util.HashSet;
import java.util.Set;

public class StupidAssemblePrinter implements IRVisitor {
    private PrintStream out;
    private Set<String> BBVisit = new HashSet<>();
    private boolean definingGlobal = false;
    private Function curFunc;

    private void AssemblePrint(IR node){
        if (node != null) node.accept(this);
    }

    public StupidAssemblePrinter(PrintStream out) {
        this.out = out;
    }


    @Override
    public void visit(IRRoot node) {
        //tmp
        definingGlobal = true;
        this.out.println("section .bss");
        node.staticDataList.forEach(x -> x.accept(this));

        this.out.println("section .data");
        node.stringMap.values().forEach(this::visit);
        definingGlobal = false;
        this.out.println();
        node.functionMap.values().forEach(this::visit);
    }


    @Override
    public void visit(Function node) {
        //Todo complete the ir builder!!
        curFunc = node;
        this.out.println("push rbp");
        this.out.println("mov rbp rsp");
        this.out.println("sub rsp " + node.funcLocalVarRegs.size() * 8);
        AssemblePrint(node.getStartBB());
    }

    @Override
    public void visit(BasicBlock node) {
        if (BBVisit.contains(node.getName())) return;
        BBVisit.add(node.getName());
        //label: "__"!
        this.out.println("__" + node.getName() + ": ");
        for (Instruction itr = node.getHeadIns(); itr!= null; itr = itr.Next()){
            AssemblePrint(itr);
        }

        this.out.println();

        //try to print
        AssemblePrint(node.getAdjacentBB());

        node.succssorBBMap.values().forEach(this::visit);
    }

    @Override
    public void visit(Binary node) {

    }

    @Override
    public void visit(Unary node) {

    }

    @Override
    public void visit(IntCompare node) {
        /*
        // R3 = R1 > R2
        // cmp  R1, R2
        // setg R3
        String asm = "\tcmp \t" + visit(node.L_operand) + ", " + visit(node.R_operand) + "\n";
        switch (node.op){
            case LS: asm = asm + "\tsetl" ; break;
            case LE: asm = asm + "\tsetle"; break;
            case GT: asm = asm + "\tsetg" ; break;
            case GE: asm = asm + "\tsetge"; break;
            case EQ: asm = asm + "\tsete" ; break;
            case NE: asm = asm + "\tsetne"; break;
        }
         */
    }

    @Override
    public void visit(Inc node) {
        this.out.print("inc ");
        AssemblePrint(node.getBody());
        this.out.println();
    }

    @Override
    public void visit(Dec node) {
        this.out.print("dec ");
        AssemblePrint(node.getBody());
        this.out.println();
    }

    @Override
    public void visit(Jump node) {
        this.out.println("jmp " + "__" + node.getNextLabel());
    }

    @Override
    public void visit(Branch node) {
        //after the branch must be the true block!!
        this.out.print("cmp ");
        AssemblePrint(node.getCond());
        this.out.println("0");
        this.out.println("je " + "__" + node.getOtherwiseLabel());
    }

    @Override
    public void visit(Return node) {

    }

    @Override
    public void visit(Call node) {

    }

    @Override
    public void visit(HeapAllocate node) {

    }

    @Override
    public void visit(Move node) {
        this.out.print("mov ");
        AssemblePrint(node.getDest());
        AssemblePrint(node.getSource());
    }

    @Override
    public void visit(Load node) {

    }

    @Override
    public void visit(Store node) {

    }

    @Override
    public void visit(IntImme node) {
        this.out.print(node.getImmeValue() + " ");
    }

    @Override
    public void visit(VirtualRegister node) {

    }

    @Override
    public void visit(PhysicalRegister node) {

    }

    @Override
    public void visit(StaticSpace node) {
        //global_var has "._"
        if (definingGlobal) {
            this.out.print("._" + node.getName() + ":");
            this.out.println("\tresq 1");
        }
        else {
            this.out.print("qword [" + "._" + node.getName() + "] ");
        }
    }

    @Override
    public void visit(StaticString node) {
        if (definingGlobal) {
            this.out.print("." + node.getName() + ": ");
            this.out.println("\tdb " + "\"" + node.getData() + "\"" + ", 0");
        }
        else{
            this.out.print("." + node.getName() + " ");
        }
    }
}
