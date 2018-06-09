package shsjxzh.compiler.BackEnd;

import shsjxzh.compiler.IR.*;
import shsjxzh.compiler.IR.Instruction.*;
import shsjxzh.compiler.IR.Value.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintStream;
import java.util.*;

import static java.lang.System.exit;

public class AssemblePrinter implements IRVisitor {
    private PrintStream out;
    private Set<String> BBVisit = new HashSet<>();
    private boolean definingGlobal = false;
    private Function curFunc;

    //int localVarSize = 0;

    //Todo : check if all has println!!
    //Todo : rax, rdx will never be used in register allocation, so you can use it with no worry

    private void AssemblePrint(IR node){
        if (node != null) node.accept(this);
    }

    public AssemblePrinter(PrintStream out) {
        this.out = out;
    }

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

        //for callee save
        //node.usedCalleePhyReg

        //although it may be wasteful, to be convenient I need it...
        this.out.println("\tsub rsp, " + node.funcLocalVarRegs.size() * 8);

        AssemblePrint(node.getStartBB());

        //to ensure safety
        this.out.println("\tleave");
        this.out.println("\tret");

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
            this.out.println();
        }
        //try to print
        if (node.getAdjacentBB() != null) {
            AssemblePrint(node.getAdjacentBB());
        }

        node.succssorBBMap.values().forEach(this::visit);
    }

    private void MemProcessBinary(Binary node) {
        Stack<PhysicalRegister> returnStack = new Stack<>();

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

                //save
                if (curFunc.funcParams.size() >= 4){
                    this.out.println("\tpush rcx");
                    returnStack.push(PhysicalRegisterSet.rcx);
                }
                if (curFunc.funcParams.size() >= 3){
                    this.out.println("\tpush rdx");
                    returnStack.push(PhysicalRegisterSet.rdx);
                }

                this.out.print("\tmov rcx, "); AssemblePrint(node.getRight()); this.out.println();
                this.out.println("\txor rdx, rdx");

                this.out.println("\tidiv rcx");
                //this.out.print("mov rax rdx");
                break;
            case Div:

                //save
                if (curFunc.funcParams.size() >= 4){
                    this.out.println("\tpush rcx");
                    returnStack.push(PhysicalRegisterSet.rcx);
                }
                if (curFunc.funcParams.size() >= 3){
                    this.out.println("\tpush rdx");
                    returnStack.push(PhysicalRegisterSet.rdx);
                }

                this.out.print("\tmov rcx, "); AssemblePrint(node.getRight()); this.out.println();
                this.out.println("\txor rdx, rdx");
                this.out.println("\tidiv rcx");
                break;
            case Shr:
                //Todo: about the "cl" register
                if (curFunc.funcParams.size() >= 4){
                    this.out.println("\tpush rcx");
                    returnStack.push(PhysicalRegisterSet.rcx);
                }

                this.out.print("\tmov rcx, "); AssemblePrint(node.getRight()); this.out.println();
                this.out.println("\tsar rax, cl");
                break;
            case Shl:
                //Todo: about the "cl" register
                if (curFunc.funcParams.size() >= 4){
                    this.out.println("\tpush rcx");
                    returnStack.push(PhysicalRegisterSet.rcx);
                }

                this.out.print("\tmov rcx, "); AssemblePrint(node.getRight()); this.out.println();
                this.out.println("\tsal rax, cl");
                break;
        }
        //save it!
        this.out.print("\tmov ");
        AssemblePrint(node.getDest());
        if (node.getOp() == Binary.BinaryOp.Mod) this.out.println(", rdx");
        else this.out.println(", rax");

        while (!returnStack.empty()){
            this.out.print("\tpop "); AssemblePrint(returnStack.pop()); this.out.println();
        }
    }

    @Override
    public void visit(Binary node) {
        if (needIntermediateReg(node.getDest(), node.getRight())){
            MemProcessBinary(node);
        }
        else{
            Stack<PhysicalRegister> returnStack = new Stack<>();
            switch (node.getOp()){
                case Add:
                    this.out.print("\tadd ");
                    AssemblePrint(node.getDest());
                    this.out.print(", ");
                    AssemblePrint(node.getRight());
                    this.out.println();
                    break;
                case Sub:
                    this.out.print("\tsub ");
                    AssemblePrint(node.getDest());
                    this.out.print(", ");
                    AssemblePrint(node.getRight());
                    this.out.println();
                    break;
                case BitAnd:
                    this.out.print("\tand ");
                    AssemblePrint(node.getDest());
                    this.out.print(", ");
                    AssemblePrint(node.getRight());
                    this.out.println();
                    break;
                case BitOr:
                    this.out.print("\tor ");
                    AssemblePrint(node.getDest());
                    this.out.print(", ");
                    AssemblePrint(node.getRight());
                    this.out.println();
                    break;
                case Xor:
                    this.out.print("\txor ");
                    AssemblePrint(node.getDest());
                    this.out.print(", ");
                    AssemblePrint(node.getRight());
                    this.out.println();
                    break;
                case Mul:
                    //Todo is it right?
                    if (OneValueNeedMem(node.getDest())){
                        this.out.print("\tmov rax, "); AssemblePrint(node.getDest()); this.out.println();
                        this.out.print("\timul rax, "); AssemblePrint(node.getRight()); this.out.println();
                        this.out.print("\tmov "); AssemblePrint(node.getDest()); this.out.println(", rax");
                    }
                    else {
                        this.out.print("\timul ");
                        AssemblePrint(node.getDest());
                        this.out.print(", ");
                        AssemblePrint(node.getRight());
                        this.out.println();
                    }
                    break;
                case Mod:
                    //Todo make sure the mod instruction!!

                    if (curFunc.funcParams.size() >= 4){
                        this.out.println("\tpush rcx");
                        returnStack.push(PhysicalRegisterSet.rcx);
                    }
                    if (curFunc.funcParams.size() >= 3){
                        this.out.println("\tpush rdx");
                        returnStack.push(PhysicalRegisterSet.rdx);
                    }

                    this.out.print("\tmov rax, "); AssemblePrint(node.getDest()); this.out.println();
                    this.out.print("\tmov rcx, "); AssemblePrint(node.getRight()); this.out.println();

                    this.out.println("\txor rdx, rdx");
                    this.out.println("\tidiv rcx");

                    //save it
                    this.out.print("\tmov "); AssemblePrint(node.getDest()); this.out.println(", rdx");
                    break;
                case Div:
                    //Todo make sure the extend instruction!!

                    if (curFunc.funcParams.size() >= 4){
                        this.out.println("\tpush rcx");
                        returnStack.push(PhysicalRegisterSet.rcx);
                    }
                    if (curFunc.funcParams.size() >= 3){
                        this.out.println("\tpush rdx");
                        returnStack.push(PhysicalRegisterSet.rdx);
                    }

                    this.out.print("\tmov rax, "); AssemblePrint(node.getDest()); this.out.println();
                    this.out.print("\tmov rcx, "); AssemblePrint(node.getRight()); this.out.println();

                    this.out.println("\txor rdx, rdx");
                    this.out.println("\tidiv rcx");

                    //save it
                    this.out.print("\tmov "); AssemblePrint(node.getDest()); this.out.println(", rax");
                    break;
                case Shr:
                    //Todo: about the "cl" register
                    if (curFunc.funcParams.size() >= 4){
                        this.out.println("\tpush rcx");
                        returnStack.push(PhysicalRegisterSet.rcx);
                    }

                    this.out.print("\tmov rcx, "); AssemblePrint(node.getRight()); this.out.println();
                    this.out.print("\tsar "); AssemblePrint(node.getDest()); this.out.println(", cl");
                    break;
                case Shl:
                    //Todo: about the "cl" register
                    if (curFunc.funcParams.size() >= 4){
                        this.out.println("\tpush rcx");
                        returnStack.push(PhysicalRegisterSet.rcx);
                    }

                    this.out.print("\tmov rcx, "); AssemblePrint(node.getRight()); this.out.println();
                    this.out.print("\tsal "); AssemblePrint(node.getDest()); this.out.println(", cl");
                    break;
            }
            while (!returnStack.empty()){
                this.out.print("\tpop "); AssemblePrint(returnStack.pop()); this.out.println();
            }
        }
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
        */
        this.out.print("\tmov rax, "); AssemblePrint(node.getLeft()); this.out.println();

        this.out.print("\tcmp rax, "); AssemblePrint(node.getRight()); this.out.println();

        String op =  null;
        switch (node.getOp()){
            case GE: op = "\tsetge "; break;
            case LE: op = "\tsetle "; break;
            case GT: op = "\tsetg ";  break;
            case LT: op = "\tsetl ";  break;
            case EQ: op = "\tsete ";  break;
            case NE: op = "\tsetne "; break;
        }
        this.out.println(op + "al");

        //Todo: trying to reduce it!!
        //why movzx don't accept the memory??
        if (curFunc.funcParams.size() >= 4){
            this.out.println("\tpush rcx");
        }

        this.out.println("\tmovzx rcx, al");
        this.out.print("\tmov "); AssemblePrint(node.getDest()); this.out.println(", rcx");

        if (curFunc.funcParams.size() >= 4){
            this.out.println("\tpop rcx");
        }
    }

    @Override
    public void visit(Inc node) {
        this.out.print("\tadd ");
        AssemblePrint(node.getBody());
        this.out.println(", 1");
    }

    @Override
    public void visit(Dec node) {
        this.out.print("\tsub ");
        AssemblePrint(node.getBody());
        this.out.println(", 1");
    }

    @Override
    public void visit(Return node) {
        if (node.getReturnValue() != null) {
            this.out.print("\tmov rax, "); AssemblePrint(node.getReturnValue()); this.out.println();
        }
        this.out.println("\tleave");
        this.out.println("\tret");
    }

    private boolean hasPhysicalReg(Value value){
        return value instanceof VirtualRegister && ((VirtualRegister) value).hasPhysicalReg();
    }

    private boolean paramNeedMove(Value paramValue, int loca){
        if (hasPhysicalReg(paramValue)) {
            PhysicalRegister tmpReg = ((VirtualRegister) paramValue).trueReg;
            switch (loca) {
                case 4:
                    return !tmpReg.equals(PhysicalRegisterSet.rcx);
                case 3:
                    return !tmpReg.equals(PhysicalRegisterSet.rdx);
                case 2:
                    return !tmpReg.equals(PhysicalRegisterSet.rsi);
                case 1:
                    return !tmpReg.equals(PhysicalRegisterSet.rdi);
                    default:
                        throw new RuntimeException("error using of paramNeedMove");
            }
        }
        return true;
    }

    private void buildInFuncProcess(Call node){
        //push
        String name = node.getCallFunc().getName();

        Stack<PhysicalRegister> callerSave = new Stack<>();
        for (VirtualRegister virReg: node.liveOut){
            if (virReg.hasPhysicalReg() && virReg.trueReg.isCallerSave() && !virReg.equals(node.getDest())){
                this.out.print("\tpush "); AssemblePrint(virReg.trueReg); this.out.println();
                callerSave.push(virReg.trueReg);
            }
        }

        boolean easyMode = true;
        for (Value value : node.getArgvs()) {
            if (value instanceof VirtualRegister){
                VirtualRegister tmp = (VirtualRegister) value;
                if (tmp.hasPhysicalReg() && PhysicalRegisterSet.FuncParamRegs.contains(tmp.trueReg)){
                    easyMode = false;
                    break;
                }
            }
        }

        if (!easyMode) {
            List<Value> reverseVar = node.getArgvs().subList(0, node.getArgvs().size());
            Collections.reverse(reverseVar);
            //int i = node.getArgvs().size();
            for (Value value : reverseVar) {
                this.out.print("\tpush ");
                AssemblePrint(value);
                this.out.println();
            }

            int i = node.getArgvs().size();
            for (PhysicalRegister funcParamReg : PhysicalRegisterSet.FuncParamRegs) {
                if (i <= 0) break;
                --i;
                this.out.print("\tpop ");
                AssemblePrint(funcParamReg);
                this.out.println();
            }
        }
        else{
            switch (node.getArgvs().size()){
                case 4:
                    if (paramNeedMove(node.getArgvs().get(3), 4)) {
                        this.out.print("\tmov rcx, ");
                        AssemblePrint(node.getArgvs().get(3));
                        this.out.println();
                    }
                case 3:
                    if (paramNeedMove(node.getArgvs().get(2), 3)) {
                        this.out.print("\tmov rdx, ");
                        AssemblePrint(node.getArgvs().get(2));
                        this.out.println();
                    }
                case 2:
                    if (paramNeedMove(node.getArgvs().get(1), 2)) {
                        this.out.print("\tmov rsi, "); AssemblePrint(node.getArgvs().get(1)); this.out.println();
                    }
                case 1:
                    if (paramNeedMove(node.getArgvs().get(0), 1)) {
                        this.out.print("\tmov rdi, "); AssemblePrint(node.getArgvs().get(0)); this.out.println();
                    }
                case 0:
                    break;
                    default: throw new RuntimeException("build in func size error");
            }
        }

        this.out.println("\tcall " + name);

        //finish the task
        while (!callerSave.empty()){
            this.out.print("\tpop "); AssemblePrint(callerSave.pop()); this.out.println();
        }

        if (node.getDest() != null) {
            this.out.print("\tmov ");
            AssemblePrint(node.getDest());
            this.out.println(", rax");
        }

    }

    @Override
    public void visit(Call node) {
        if (node.getCallFunc().isBuildIn()){
            buildInFuncProcess(node);
        }
        else{

            Stack<PhysicalRegister> callerSave = new Stack<>();
            for (VirtualRegister virReg: node.liveOut){
                if (virReg.hasPhysicalReg() && virReg.trueReg.isCallerSave() && !virReg.equals(node.getDest())){
                    this.out.print("\tpush "); AssemblePrint(virReg.trueReg); this.out.println();
                    callerSave.push(virReg.trueReg);
                }
            }

            boolean easyMode = true;
            for (Value value : node.getArgvs()) {
                if (value instanceof VirtualRegister){
                    VirtualRegister tmp = (VirtualRegister) value;
                    if (tmp.hasPhysicalReg() && PhysicalRegisterSet.FuncParamRegs.contains(tmp.trueReg)){
                        easyMode = false;
                        break;
                    }
                }
            }

            List<Value> reverseVar = node.getArgvs().subList(0, node.getArgvs().size());
            Collections.reverse(reverseVar);
            if (!easyMode) {
                //int i = node.getArgvs().size();
                for (Value value : reverseVar) {
                    this.out.print("\tpush ");
                    AssemblePrint(value);
                    this.out.println();
                }

                int i = node.getArgvs().size();
                for (PhysicalRegister funcParamReg : PhysicalRegisterSet.FuncParamRegs) {
                    if (i <= 0) break;
                    --i;
                    this.out.print("\tpop ");
                    AssemblePrint(funcParamReg);
                    this.out.println();
                }
            }
            else{
                int i = node.getArgvs().size();
                for (Value value : reverseVar) {
                    if (i >= 5) {
                        this.out.print("\tpush ");
                        AssemblePrint(value);
                        this.out.println();
                    }
                    else {
                        switch (i){
                            case 4:
                                if (paramNeedMove(value, 4)) {
                                    this.out.print("\tmov rcx, ");
                                    AssemblePrint(value);
                                    this.out.println();
                                }
                                break;
                            case 3:
                                if (paramNeedMove(value, 3)) {
                                    this.out.print("\tmov rdx, ");
                                    AssemblePrint(value);
                                    this.out.println();
                                }
                                break;
                            case 2:
                                if (paramNeedMove(value, 2)) {
                                    this.out.print("\tmov rsi, "); AssemblePrint(value); this.out.println();
                                }
                                break;
                            case 1:
                                if (paramNeedMove(value, 1)) {
                                    this.out.print("\tmov rdi, "); AssemblePrint(value); this.out.println();
                                }
                                break;
                            default: throw new RuntimeException("self defined func call error");
                        }
                    }
                    --i;
                }
            }


            this.out.println("\tcall @" + node.getCallFunc().getName());
            if (node.getArgvs().size() >= 5) {
                this.out.println("\tadd rsp, " + (node.getArgvs().size() - 4) * 8);
            }

            //finish the task
            while (!callerSave.empty()){
                this.out.print("\tpop "); AssemblePrint(callerSave.pop()); this.out.println();
            }
            if (node.getDest() != null) {
                this.out.print("\tmov "); AssemblePrint(node.getDest()); this.out.println(", rax");
            }

            /*else {
                switch (i){
                    case 4:
                        if (paramNeedMove(value, 4)) {
                            this.out.print("\tmov rcx, ");
                            AssemblePrint(value);
                            this.out.println();
                        }
                        break;
                    case 3:
                        if (paramNeedMove(value, 3)) {
                            this.out.print("\tmov rdx, ");
                            AssemblePrint(value);
                            this.out.println();
                        }
                        break;
                    case 2:
                        if (paramNeedMove(value, 2)) {
                            this.out.print("\tmov rsi, "); AssemblePrint(value); this.out.println();
                        }
                        break;
                    case 1:
                        if (paramNeedMove(value, 1)) {
                            this.out.print("\tmov rdi, "); AssemblePrint(value); this.out.println();
                        }
                        break;
                    default: throw new RuntimeException("self defined func call error");
                }
            }
            --i;*/
        }
    }

    @Override
    public void visit(HeapAllocate node) {
        Stack<PhysicalRegister> callerSave = new Stack<>();
        for (VirtualRegister virReg: node.liveOut){
            if (virReg.hasPhysicalReg() && virReg.trueReg.isCallerSave() && !virReg.equals(node.getDest())){
                this.out.print("\tpush "); AssemblePrint(virReg.trueReg); this.out.println();
                callerSave.push(virReg.trueReg);
            }
        }

        this.out.print("\tmov rdi, "); AssemblePrint(node.getSize()); this.out.println();
        this.out.println("\tcall malloc");

        while (!callerSave.empty()){
            this.out.print("\tpop "); AssemblePrint(callerSave.pop()); this.out.println();
        }

        this.out.print("\tmov "); AssemblePrint(node.getDest()); this.out.println(", rax");
    }

    private boolean OneValueNeedMem(Value value){
        if (value instanceof VirtualRegister && !((VirtualRegister) value).hasPhysicalReg()) return true;
        if (value instanceof StaticData) return true;
        return false;
    }

    private boolean needIntermediateReg(Value left, Value right){
        return  (OneValueNeedMem(left)  && OneValueNeedMem(right)) ;
    }

    @Override
    public void visit(Move node) {
        if (node.getDest() instanceof VirtualRegister && node.getSource() instanceof VirtualRegister){
            if (((VirtualRegister) node.getDest()).hasPhysicalReg() && ((VirtualRegister) node.getSource()).hasPhysicalReg()){
                if (((VirtualRegister) node.getDest()).trueReg.equals(((VirtualRegister) node.getSource()).trueReg)){
                    return;
                }
            }
        }
        if (needIntermediateReg(node.getDest(), node.getSource())){
            this.out.print("\tmov rax, ");
            AssemblePrint(node.getSource());
            this.out.println();

            this.out.print("\tmov ");
            AssemblePrint(node.getDest());
            this.out.println(", rax");
        }
        else{
            this.out.print("\tmov "); AssemblePrint(node.getDest()); this.out.print(",");
            AssemblePrint(node.getSource()); this.out.println();
        }
    }

    @Override
    public void visit(Load node) {
        boolean baseUseIntermediate  = true;
        boolean indexUseIntermediate = true;
        Stack<PhysicalRegister> returnStack = new Stack<>();
        if (node.getBase() != null) {
            if (hasPhysicalReg(node.getBase())){
                baseUseIntermediate = false;
            }
            else {

                if (curFunc.funcParams.size() >= 4){
                    this.out.println("\tpush rcx");
                    returnStack.push(PhysicalRegisterSet.rcx);
                }

                this.out.print("\tmov rcx, ");
                AssemblePrint(node.getBase());
                this.out.println();
            }
        }
        if (node.getIndex() != null) {
            if (hasPhysicalReg(node.getIndex())) {
                indexUseIntermediate = false;
            }
            else {

                if (curFunc.funcParams.size() >= 3){
                    this.out.println("\tpush rdx");
                    returnStack.push(PhysicalRegisterSet.rdx);
                }

                this.out.print("\tmov rdx, ");
                AssemblePrint(node.getIndex());
                this.out.println();
            }
        }

        if (OneValueNeedMem(node.getDest())){
            this.out.print("\tmov rax, qword [");
            if (node.getBase()!= null){
                if (baseUseIntermediate) this.out.print("rcx ");
                else AssemblePrint(node.getBase());
            }
            if (node.getIndex() != null){
                if (indexUseIntermediate) this.out.print( "+ " + "rdx * " + node.getScale());
                else{
                    this.out.print( "+ ");
                    AssemblePrint(node.getIndex());
                    this.out.print("* " + node.getScale());
                }
            }
            if (node.getDisplacement() != null){
                this.out.print("+ ");
                AssemblePrint(node.getDisplacement());
            }
            this.out.println("] ");

            //Todo check the def use in load store
            this.out.print("\tmov "); AssemblePrint(node.getDest()); this.out.println(", rax");
        }
        else{
            this.out.print("\tmov "); AssemblePrint(node.getDest()); this.out.print(", qword [");
            if (node.getBase()!= null){
                if (baseUseIntermediate) this.out.print("rcx ");
                else AssemblePrint(node.getBase());
            }
            if (node.getIndex() != null){
                if (indexUseIntermediate) this.out.print( "+ " + "rdx * " + node.getScale());
                else{
                    this.out.print( "+ ");
                    AssemblePrint(node.getIndex());
                    this.out.print("* " + node.getScale());
                }
            }
            if (node.getDisplacement() != null){
                this.out.print("+ ");
                AssemblePrint(node.getDisplacement());
            }
            this.out.println("] ");
        }

        while (!returnStack.empty()){
            this.out.print("\tpop "); AssemblePrint(returnStack.pop()); this.out.println();
        }
    }

    @Override
    public void visit(Store node) {
        boolean baseUseIntermediate  = true;
        boolean indexUseIntermediate = true;
        Stack<PhysicalRegister> returnStack = new Stack<>();
        if (node.getBase() != null) {
            if (hasPhysicalReg(node.getBase())){
                baseUseIntermediate = false;
            }
            else {

                if (curFunc.funcParams.size() >= 4){
                    this.out.println("\tpush rcx");
                    returnStack.push(PhysicalRegisterSet.rcx);
                }

                this.out.print("\tmov rcx, ");
                AssemblePrint(node.getBase());
                this.out.println();
            }
        }
        if (node.getIndex() != null) {
            if (hasPhysicalReg(node.getIndex())) {
                indexUseIntermediate = false;
            }
            else {

                if (curFunc.funcParams.size() >= 3){
                    this.out.println("\tpush rdx");
                    returnStack.push(PhysicalRegisterSet.rdx);
                }

                this.out.print("\tmov rdx, ");
                AssemblePrint(node.getIndex());
                this.out.println();
            }
        }

        if (OneValueNeedMem(node.getSource())) {
            //need mem
            this.out.print("\tmov rax, "); AssemblePrint(node.getSource()); this.out.println();
        }

        this.out.print("\tmov qword [");
        if (node.getBase()!= null){
            if (baseUseIntermediate) this.out.print("rcx ");
            else AssemblePrint(node.getBase());
        }
        if (node.getIndex() != null){
            if (indexUseIntermediate) this.out.print( "+ " + "rdx * " + node.getScale());
            else{
                this.out.print( "+ ");
                AssemblePrint(node.getIndex());
                this.out.print("* " + node.getScale());
            }
        }
        if (node.getDisplacement() != null){
            this.out.print("+ ");
            AssemblePrint(node.getDisplacement());
        }
        this.out.print("], ");

        if (OneValueNeedMem(node.getSource())){
            //need mem
            this.out.println("rax");
        }
        else {
            AssemblePrint(node.getSource());
            this.out.println();
        }

        while (!returnStack.empty()){
            this.out.print("\tpop "); AssemblePrint(returnStack.pop()); this.out.println();
        }
    }

    //========================== variable ==============================

    @Override
    public void visit(IntImme node) {
        this.out.print(node.getImmeValue() + " ");
    }

    @Override
    public void visit(VirtualRegister node) {
        if (node.hasPhysicalReg()) AssemblePrint(node.trueReg);
        else{
            if (curFunc.funcLocalVarRegs.containsKey(node.getName())) {
                this.out.print(("qword [rbp - " + (curFunc.localVarPos.get(node.getName()) + 8) + "] "));  //
            }

            else if (curFunc.funcParams.containsKey(node.getName())) {
                this.out.print("qword [rbp + " + (16 + curFunc.paramPos.get(node.getName()) - 8 * PhysicalRegisterSet.FuncParamRegs.size()) + "] ");
            }
        }
    }

    @Override
    public void visit(PhysicalRegister node) {
        this.out.print(node.getName() + " ");
    }


    //========================== condition ==============================


    @Override
    public void visit(Jump node) {
        if (node.getBelongBB().getAdjacentBB() != null){
            String nextOutBB = node.getBelongBB().getAdjacentBB().getName();
            if (node.getNextLabel().equals(nextOutBB) && !BBVisit.contains(nextOutBB)) return;
        }
        this.out.println("\tjmp " + "__" + node.getNextLabel());
    }

    @Override
    public void visit(Branch node) {
        //after the branch must be the true block!!
        this.out.print("\tcmp ");
        AssemblePrint(node.getCond());
        this.out.println(", 0");
        this.out.println("\tje " + "__" + node.getOtherwiseLabel());
        if (BBVisit.contains(node.getThenLabel())){
            this.out.println("\tjmp "+ "__" + node.getThenLabel());
        }
    }

    //========================== static ==============================

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
            //String is "@_"
            this.out.println("@_" + node.getName() + ": ");

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
            this.out.print("@_" + node.getName() + " ");
        }
    }
}
