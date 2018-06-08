package shsjxzh.compiler.BackEnd;

import shsjxzh.compiler.IR.*;
import shsjxzh.compiler.IR.Instruction.*;
import shsjxzh.compiler.IR.Value.*;

import java.io.PrintStream;
import java.util.HashSet;
import java.util.Set;

public class livenessPrinter implements IRVisitor {
    private String indent;
    private PrintStream out;
    private Set<String> BBVisit = new HashSet<>();
    private boolean definingGlobal = false;

    public livenessPrinter(PrintStream out) {
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
        definingGlobal = true;
        node.staticDataList.forEach(x -> x.accept(this));
        node.stringMap.values().forEach(this::visit);
        definingGlobal = false;
        this.out.println();
        node.functionMap.values().forEach(this::visit);
    }

    @Override
    public void visit(Function node) {
        if (node.isBuildIn()) return;
        myPrint(node.getName() + ": ");
        /*for (VirtualRegister funcParam : node.funcParams) {
            PrintIR(funcParam);
        }*/
        node.funcParams.values().forEach(x -> PrintIR(x));

        this.out.println();
        addIndent();
        PrintIR(node.getStartBB());
        subIndent();
    }

    @Override
    public void visit(BasicBlock node) {
        if (BBVisit.contains(node.getName())) return;
        BBVisit.add(node.getName());
        //myPrintln("");
        //if (node != node.getBelongFunc().getStartBB()) myPrintln("");
        myPrintln(node.getName() + ": ");
        for (Instruction itr = node.getHeadIns(); itr!= null; itr = itr.Next()){
            PrintIR(itr);
            myPrint("live in: "); itr.liveIn.forEach(x -> PrintIR(x)); myPrintln("");
            myPrint("live out: "); itr.liveOut.forEach(x -> PrintIR(x)); myPrintln("");
        }

        this.out.println();

        //try to print
        PrintIR(node.getAdjacentBB());

        node.succssorBBMap.values().forEach(this::visit);
    }

    @Override
    public void visit(Binary node) {
        myPrint("");
        //the print of reg will do nothing with the indent
        PrintIR(node.getDest());

        switch (node.getOp()){
            case Add:
                this.out.print("= add ");
                break;
            case Sub:
                this.out.print("= sub ");
                break;
            case Mul:
                this.out.print("= mul ");
                break;
            case Div:
                this.out.print("= div ");
                break;
            case Mod:
                this.out.print("= mod ");
                break;
            case Shl:
                this.out.print("= shl ");
                break;
            case Shr:
                this.out.print("= shr ");
                break;
            case Xor:
                this.out.print("= xor ");
                break;
            case BitOr:
                this.out.print("= bitOr ");
                break;
            case BitAnd:
                this.out.print("= bitAnd ");
                break;
        }
        PrintIR(node.getRight());

        this.out.println();
    }

    @Override
    public void visit(Unary node) {
        myPrint("");
        //the print of reg will do nothing with the indent
        PrintIR(node.getDest());
        switch (node.getOp()){
            case BitNot:
                this.out.print("= bitNot ");
                break;
            case Neg:
                this.out.print("= neg ");
                break;
        }
        PrintIR(node.getDest());
        this.out.println();
    }

    @Override
    public void visit(IntCompare node) {
        myPrint("");
        PrintIR(node.getDest());
        this.out.print("= ");
        PrintIR(node.getLeft());
        switch (node.getOp()){
            case LE:
                this.out.print("LE ");
                break;
            case GE:
                this.out.print("GE ");
                break;
            case LT:
                this.out.print("LT ");
                break;
            case GT:
                this.out.print("GT ");
                break;
            case EQ:
                this.out.print("EQ ");
                break;
            case NE:
                this.out.print("NE ");
                break;
        }
        PrintIR(node.getRight());
        this.out.println();
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
        this.out.println();
    }

    @Override
    public void visit(Call node) {
        myPrint("");
        if (node.getDest() != null) {
            PrintIR(node.getDest());
            this.out.print("= ");
        }

        this.out.print("call " + node.getCallFunc().getName() + " ");
        for (Value value : node.getArgvs()) {
            PrintIR(value);
        }
        this.out.println();
    }

    @Override
    public void visit(HeapAllocate node) {
        myPrint("");
        //the print of reg will do nothing with the indent
        PrintIR(node.getDest());
        this.out.print("= heapAlloc ");
        PrintIR(node.getSize());
        this.out.println();
    }

    @Override
    public void visit(Move node) {
        myPrint("");
        //the print of reg will do nothing with the indent
        PrintIR(node.getDest());
        this.out.print("= move ");
        PrintIR(node.getSource());
        this.out.println();
    }

    @Override
    public void visit(Load node) {
        myPrint("");
        PrintIR(node.getDest());
        this.out.print("= load ");
        PrintIR(node.getBase());
        if (node.getIndex() != null) {
            this.out.print("+ " + node.getScale() + " * ");
            PrintIR(node.getIndex());
        }
        if (node.getDisplacement() != null){
            if(node.getDisplacement().getImmeValue() >= 0) this.out.print("+ ");
            PrintIR(node.getDisplacement());
        }
        this.out.println();
    }

    @Override
    public void visit(Store node) {
        myPrint("Store ");
        PrintIR(node.getSource());
        this.out.print("-> *( ");
        PrintIR(node.getBase());
        if (node.getIndex() != null) {
            this.out.print("+ " + node.getScale() + " * ");
            PrintIR(node.getIndex());
        }
        if (node.getDisplacement() != null){
            if(node.getDisplacement().getImmeValue() >= 0) this.out.print("+ ");
            PrintIR(node.getDisplacement());
        }
        this.out.println(")");
    }

    @Override
    public void visit(Inc node) {
        myPrint("Inc ");
        PrintIR(node.getBody());
        this.out.println();
    }

    @Override
    public void visit(Dec node) {
        myPrint("Dec ");
        PrintIR(node.getBody());
        this.out.println();
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
        if (definingGlobal) {
            this.out.print("._" + node.getName() + ":");
            this.out.println("\tresb " + node.getSpaceSize());
        }
        else {
            this.out.print("._" + node.getName() + " ");
        }
    }

    @Override
    public void visit(StaticString node) {
        //str has  "." as the start
        if (definingGlobal) {
            this.out.print("." + node.getName() + ": ");
            this.out.println("\tdb " + "\"" + node.getData() + "\"" + ", 0");
        }
        else {
            this.out.print("." + node.getName() + " ");
        }
    }
}
