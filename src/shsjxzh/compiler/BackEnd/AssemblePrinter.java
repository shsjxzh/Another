package shsjxzh.compiler.BackEnd;

import shsjxzh.compiler.IR.*;
import shsjxzh.compiler.IR.Instruction.*;
import shsjxzh.compiler.IR.Value.*;

import java.io.PrintStream;
import java.util.HashSet;
import java.util.Set;

public class AssemblePrinter implements IRVisitor {
    private PrintStream out;
    private Set<String> BBVisit = new HashSet<>();
    private boolean definingGlobal = false;

    //int localVarSize = 0;

    private void AssemblePrint(IR node){
        if (node != null) node.accept(this);
    }

    public AssemblePrinter(PrintStream out) {
        this.out = out;
    }

    @Override
    public void visit(Function node) {

    }

    @Override
    public void visit(BasicBlock node) {

    }

    @Override
    public void visit(IRRoot node) {

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
    public void visit(Inc node) {
        this.out.print("INC");
        AssemblePrint(node.getBody());
        this.out.println();
    }

    @Override
    public void visit(Dec node) {
        this.out.print("DEC");
        AssemblePrint(node.getBody());
        this.out.println();
    }

    @Override
    public void visit(Jump node) {
        this.out.println("JMP " + node.getNextLabel());
    }

    @Override
    public void visit(Branch node) {

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

    }

    @Override
    public void visit(Load node) {

    }

    @Override
    public void visit(Store node) {

    }

    @Override
    public void visit(IntImme node) {

    }

    @Override
    public void visit(VirtualRegister node) {

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
