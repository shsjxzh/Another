package shsjxzh.compiler.BackEnd;

import shsjxzh.compiler.IR.*;
import shsjxzh.compiler.IR.Instruction.*;
import shsjxzh.compiler.IR.Value.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintStream;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.lang.System.exit;

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

    //Todo: the final result must be saved in rax!!

    @Override
    public void visit(IRRoot node) {
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader("lib/lib.asm"));
            String line;
            while ((line = br.readLine()) != null){
                this.out.println(line);
            }

        } catch (Exception e) {
            e.printStackTrace();
            exit(0);
        }

        //main
        this.out.println(";===================================================\n");
        Function initFunc = null;
        for (Function function : node.functionMap.values()) {
            if (!function.getName().equals("__init")){
                AssemblePrint(function);
            }
            else initFunc = function;
        }

        AssemblePrint(initFunc);

        //data
        try {
            br = new BufferedReader(new FileReader("lib/data.asm"));
            String line;
            while ((line = br.readLine()) != null){
                this.out.println(line);
            }

        } catch (Exception e) {
            e.printStackTrace();
            exit(0);
        }
        definingGlobal = true;
        //this.out.println("section .data");
        node.stringMap.values().forEach(this::visit);
        this.out.println();

        //bss
        try {
            br = new BufferedReader(new FileReader("lib/bss.asm"));
            String line;
            while ((line = br.readLine()) != null){
                this.out.println(line);
            }

        } catch (Exception e) {
            e.printStackTrace();
            exit(0);
        }
        //this.out.println("section .bss");
        node.staticDataList.forEach(x -> x.accept(this));
        this.out.println();
        definingGlobal = false;

        //end
        try {
            br = new BufferedReader(new FileReader("lib/end.asm"));
            String line;
            while ((line = br.readLine()) != null){
                this.out.println(line);
            }

        } catch (Exception e) {
            e.printStackTrace();
            exit(0);
        }
    }


    @Override
    public void visit(Function node) {
        //for self define function, add "@"
        curFunc = node;

        if(node.getName().equals("__init")) this.out.println( "main: ");
        else this.out.println("@" + node.getName() + ": ");

        this.out.println("\tpush rbp");
        this.out.println("\tmov rbp, rsp");

        //global variable doesn't need it
        if (!node.getName().equals("__init")) this.out.println("\tsub rsp, " + node.funcLocalVarRegs.size() * 8);

        //for local variable
        //this.out.println("\tmov r11, rbp");
        //this.out.println("\tadd r11, 16");

        AssemblePrint(node.getStartBB());

        this.out.println();
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

        //this.out.println();

        //try to print
        AssemblePrint(node.getAdjacentBB());

        node.succssorBBMap.values().forEach(this::visit);
    }

    @Override
    public void visit(Binary node) {
        this.out.print("\tmov rax, ");
        AssemblePrint(node.getDest());
        this.out.println();

        switch (node.getOp()){
            case Add:
                this.out.print("\tadd rax, ");
                AssemblePrint(node.getRight());
                this.out.println();
                break;
            case Sub:
                this.out.print("\tsub rax, ");
                AssemblePrint(node.getRight());
                this.out.println();
                break;
            case BitAnd:
                this.out.print("\tand rax, ");
                AssemblePrint(node.getRight());
                this.out.println();
                break;
            case BitOr:
                this.out.print("\tor rax, ");
                AssemblePrint(node.getRight());
                this.out.println();
                break;
            case Xor:
                this.out.print("\txor rax, ");
                AssemblePrint(node.getRight());
                this.out.println();
                break;
            case Mul:
                //Todo is it right?
                this.out.print("\timul rax, ");
                AssemblePrint(node.getRight());
                this.out.println();
                break;
            case Mod:
                //Todo make sure the mod instruction!!
                this.out.println("\tcltq");
                this.out.print("\tmov rcx, "); AssemblePrint(node.getRight()); this.out.println();
                this.out.println("\tidiv rcx");
                //this.out.print("mov rax rdx");
                break;
            case Div:
                //Todo make sure the extend instruction!!
                this.out.println("\tcltq");
                this.out.print("\tmov rcx, "); AssemblePrint(node.getRight()); this.out.println();
                this.out.println("\tidiv rcx");
                break;
            case Shr:
                //Todo: about the "cl" register
                this.out.print("\tmov rcx, "); AssemblePrint(node.getRight()); this.out.println();
                this.out.println("\tsar rax, cl");
                break;
            case Shl:
                //Todo: about the "cl" register
                this.out.print("\tmov rcx, "); AssemblePrint(node.getRight()); this.out.println();
                this.out.println("\tsal rax, cl");
                break;
        }
        //save it!
        this.out.print("\tmov ");
        AssemblePrint(node.getDest());
        if (node.getOp() == Binary.BinaryOp.Mod) this.out.println(", rdx");
        else this.out.println(", rax");
    }

    @Override
    public void visit(Unary node) {
        switch (node.getOp()){
            case Neg:
                this.out.print("\tneg ");
                break;
            case BitNot:
                this.out.print("\tnot ");
        }
        AssemblePrint(node.getDest());
        this.out.println();
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
        this.out.print("\tmov rax, "); AssemblePrint(node.getLeft()); this.out.println();

        this.out.print("\tmov rcx, "); AssemblePrint(node.getRight()); this.out.println();

        this.out.println("\tcmp rax, rcx");

        String op =  null;
        switch (node.getOp()){
            case GE: op = "\tsetge "; break;
            case LE: op = "\tsetle "; break;
            case GT: op = "\tsetg ";  break;
            case LT: op = "\tsetl ";  break;
            case EQ: op = "\tsete ";  break;
            case NE: op = "\tsetne "; break;
        }
        this.out.println(op + "rax");
        this.out.print("\tmov "); AssemblePrint(node.getDest()); this.out.println(", rax");
    }

    @Override
    public void visit(Inc node) {
        this.out.print("\tinc ");
        AssemblePrint(node.getBody());
        this.out.println();
    }

    @Override
    public void visit(Dec node) {
        this.out.print("\tdec ");
        AssemblePrint(node.getBody());
        this.out.println();
    }

    @Override
    public void visit(Jump node) {
        this.out.println("\tjmp " + "__" + node.getNextLabel());
    }

    @Override
    public void visit(Branch node) {
        //after the branch must be the true block!!
        this.out.print("\tcmp ");
        AssemblePrint(node.getCond());
        this.out.println(", 0");
        this.out.println("\tje " + "__" + node.getOtherwiseLabel());
    }

    @Override
    public void visit(Return node) {
        //the rax has already been put as the result
        this.out.println("\tleave");
        this.out.println("\tret");
    }

    private void buildInFuncCall(Call node){
        String name = node.getCallFunc().getName();
        switch (node.getArgvs().size()){
            case 3:
                this.out.print("\tmov rdx, "); AssemblePrint(node.getArgvs().get(2)); this.out.println();
            case 2:
                this.out.print("\tmov rsi, "); AssemblePrint(node.getArgvs().get(1)); this.out.println();
            case 1:
                this.out.print("mov rdi, "); AssemblePrint(node.getArgvs().get(0)); this.out.println();
            case 0:
                break;
                default: throw new RuntimeException("error buildin func params");
        }
        this.out.println("\tcall " + name);
        this.out.print("\tmov "); AssemblePrint(node.getDest()); this.out.println(", rax");
        //this.out.println("add esp " + (node.getArgvs().size() * 8));
    }

    @Override
    public void visit(Call node) {
        if (node.getCallFunc().isBuildIn()){
            buildInFuncCall(node);
        }
        else{
            List<Value> reverseVar = node.getArgvs().subList(0, node.getArgvs().size());
            Collections.reverse(reverseVar);
            for (Value value : reverseVar) {
                this.out.print("\tmov rax, "); AssemblePrint(value); this.out.println();
                this.out.println("\tpush rax");
            }
            //function name must be add @!
            this.out.println("\tcall @" + node.getCallFunc().getName());
            this.out.print("\tmov "); AssemblePrint(node.getDest()); this.out.println(", rax");
            this.out.println("\tadd rsp, " + (node.getArgvs().size() * 8));
            //Then call
            //Then clean the rubbish
        }
    }

    @Override
    public void visit(HeapAllocate node) {
        this.out.print("\tmov rdi, "); AssemblePrint(node.getSize()); this.out.println();
        this.out.println("\tcall malloc");
        this.out.print("\tmov "); AssemblePrint(node.getDest()); this.out.println(", rax");
    }

    @Override
    public void visit(Move node) {
        this.out.print("\tmov rax, ");
        AssemblePrint(node.getSource());
        this.out.println();

        this.out.print("\tmov ");
        AssemblePrint(node.getDest());
        this.out.println(", rax");
    }

    @Override
    public void visit(Load node) {
        if (node.getBase() != null) {
            this.out.print("\tmov rcx, ");
            AssemblePrint(node.getBase());
            this.out.println();
        }
        if (node.getIndex() != null) {
            this.out.print("\tmov rdx, ");
            AssemblePrint(node.getIndex());
            this.out.println();
        }
        this.out.print("\tmove rax, [");
        if (node.getBase()!= null){
            this.out.print("rcx ");
        }
        if (node.getIndex() != null){
            this.out.print( "+ " + node.getScale() + "* rdx");
        }
        if (node.getDisplacement() != null){
            this.out.print("+ ");
            AssemblePrint(node.getDisplacement());
        }
        this.out.println("]");

        this.out.print("\tmov "); AssemblePrint(node.getDest()); this.out.println(", rax");
    }

    @Override
    public void visit(Store node) {
        if (node.getBase() != null) {
            this.out.print("\tmov rcx, ");
            AssemblePrint(node.getBase());
            this.out.println();
        }
        if (node.getIndex() != null) {
            this.out.print("\tmov rdx, ");
            AssemblePrint(node.getIndex());
            this.out.println();
        }
        this.out.print("\tmov rax, ");
        AssemblePrint(node.getSource());
        this.out.println();

        this.out.print("\tmov [");
        if (node.getBase()!= null){
            this.out.print("rcx ");
        }
        if (node.getIndex() != null){
            this.out.print( "+ " + node.getScale() + "* rdx");
        }
        if (node.getDisplacement() != null){
            this.out.print("+ ");
            AssemblePrint(node.getDisplacement());
        }
        this.out.println("], rax");
    }

    @Override
    public void visit(IntImme node) {
        this.out.print(node.getImmeValue() + " ");
    }

    @Override
    public void visit(VirtualRegister node) {
        if (curFunc.funcParams.containsKey(node.getName())) {
            this.out.print("qword [rbp + " + (16 + curFunc.paramPos.get(node.getName())) + "] ");
        } else if (curFunc.funcLocalVarRegs.containsKey(node.getName())) {
            //if (curFunc.localVarPos.get(node.getName()) != 0)
            this.out.print(("[rbp - " + (curFunc.localVarPos.get(node.getName()) + 8) + "] "));  //qword
            //else
                //this.out.print(("[rbp]"));  //qword
        }
        else {
            throw new RuntimeException("register loss!");
        }
    }

    @Override
    public void visit(PhysicalRegister node) {
        //no operation
    }

    @Override
    public void visit(StaticSpace node) {
        //global_var has "$"
        if (definingGlobal) {
            this.out.println("$" + node.getName() + ":");
            this.out.println("\t\tresq\t1");
        }
        else {
            this.out.print("qword [" + "$" + node.getName() + "] ");
        }
    }

    @Override
    public void visit(StaticString node) {
        if (definingGlobal) {
            this.out.println("." + node.getName() + ": ");

            this.out.println("\tdq " + node.getData().length());

            this.out.print("\tdb " + "\"");//+ node.getData()
            String str = node.getData();
            for(int i = 0; i < str.length(); ++i){
                char ch = str.charAt(i);
                if (ch == '\\'){
                    ++i;
                    ch = str.charAt(i);
                    if (ch == 'n'){
                        this.out.print("\", 10, \"");
                    }
                    else if (ch == '\\'){
                        this.out.print("\", 92, \"");
                    }
                    else if (ch == '\"'){
                        this.out.print("\", 34, \"");
                    }
                    else {
                        throw new RuntimeException("no such esc");
                    }
                }
                else{
                    this.out.print(ch);
                }
            }

            this.out.println("\"" + ", 0");
        }
        else{
            this.out.print("." + node.getName() + " ");
        }
    }
}
