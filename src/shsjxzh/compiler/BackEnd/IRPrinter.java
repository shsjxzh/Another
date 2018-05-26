package shsjxzh.compiler.BackEnd;

import shsjxzh.compiler.IR.*;
import shsjxzh.compiler.IR.Instruction.*;
import shsjxzh.compiler.IR.Value.*;

import java.io.PrintStream;
import java.util.HashSet;
import java.util.Set;

public class IRPrinter implements IRVisitor {
    private String indent;
    private PrintStream out;
    private Set<String> BBVisit = new HashSet<>();

    public IRPrinter(PrintStream out) {
        indent = "";
        this.out = out;
    }

    private void PrintIR(IR node){
        if (node != null){
            node.accept(this);
        }
    }

    private void addIndent() {indent += "\t";}
    private void subIndent() {indent = indent.substring(1);}
    private void myPrintln(String s) {this.out.println(indent + s);}
    private void myPrint(String s) {this.out.print(indent + s);}

    @Override
    public void visit(IRRoot node) {
        //tmp
        node.functionMap.values().forEach(this::visit);
    }

    @Override
    public void visit(Function node) {
        myPrint(node.getName() + ": ");
        for (VirtualRegister funcParam : node.funcParams) {
            PrintIR(funcParam);
        }
        myPrintln("");
        addIndent();
        PrintIR(node.getStartBB());
        subIndent();
    }

    @Override
    public void visit(BasicBlock node) {
        if (BBVisit.contains(node.getName())) return;
        BBVisit.add(node.getName());
        //myPrintln("");
        if (node != node.getBelongFunc().getStartBB()) myPrintln("");
        myPrintln(node.getName() + ": ");
        for (Instruction itr = node.getHeadIns(); itr!= null; itr = itr.Next()){
            PrintIR(itr);
        }

        //try to print
        PrintIR(node.getAdjacentBB());

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

    }

    @Override
    public void visit(Jump node) {
        myPrintln("jump " + node.getNextLabel());
        //PrintIR(node.getBelongBB().succssorBBMap.get(node.getNextLabel()));
    }

    @Override
    public void visit(Branch node) {
        myPrint("branch ");
        PrintIR(node.getCond());
        this.out.println(node.getThenLabel() + " " + node.getOtherwiseLabel());
        //PrintIR(node.getThen());
        //PrintIR(node.getOtherwise());
    }

    @Override
    public void visit(Return node) {
        myPrint("ret ");
        PrintIR(node.getReturnValue());
        myPrintln("");
    }

    @Override
    public void visit(Call node) {
        myPrint("");
        PrintIR(node.getDest());
        this.out.print("= call " + node.getCallFunc().getName());
        for (Value value : node.getArgvs()) {
            PrintIR(value);
        }
        myPrintln("");
    }

    @Override
    public void visit(HeapAllocate node) {

    }

    @Override
    public void visit(Move node) {
        myPrint("");
        //the print of reg will do nothing with the indent
        PrintIR(node.getDest());
        this.out.print("= move ");
        PrintIR(node.getSource());
        myPrintln("");
    }

    @Override
    public void visit(Load node) {

    }

    @Override
    public void visit(Store node) {

    }

    @Override
    public void visit(IntImme node) {
        this.out.print(Integer.toString(node.getImmeValue()) + " ");
    }

    @Override
    public void visit(VirtualRegister node) {
        this.out.print(node.getName() + " ");
    }

    @Override
    public void visit(PhysicalRegister node) {

    }

    @Override
    public void visit(StaticSpace node) {

    }

    @Override
    public void visit(StaticString node) {

    }
}
