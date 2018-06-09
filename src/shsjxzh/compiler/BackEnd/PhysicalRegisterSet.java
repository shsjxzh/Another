package shsjxzh.compiler.BackEnd;

import java.util.*;
import shsjxzh.compiler.IR.Value.PhysicalRegister;

public class PhysicalRegisterSet {
    //never callee save
    public static final PhysicalRegister rax = new PhysicalRegister(0, "rax", true, false);

    public static final PhysicalRegister rbx = new PhysicalRegister(1, "rbx", false, true);
    //never callee save
    public static final PhysicalRegister rcx = new PhysicalRegister(2, "rcx", true, false);
    //never callee save
    public static final PhysicalRegister rdx = new PhysicalRegister(3, "rdx", true, false);

    public static final PhysicalRegister rsi = new PhysicalRegister(4, "rsi", true, false);
    public static final PhysicalRegister rdi = new PhysicalRegister(5, "rdi", true, false);
    public static final PhysicalRegister rbp = new PhysicalRegister(6, "rbp", false, true);
    public static final PhysicalRegister rsp = new PhysicalRegister(7, "rsp", false, true);

    //The above will never join the register allocation(except function params)

    public static final PhysicalRegister r8 = new PhysicalRegister(8, "r8", true, false);
    public static final PhysicalRegister r9 = new PhysicalRegister(9, "r9", true, false);
    public static final PhysicalRegister r10 = new PhysicalRegister(10, "r10", true, false);
    public static final PhysicalRegister r11 = new PhysicalRegister(11, "r11", true, false);
    public static final PhysicalRegister r12 = new PhysicalRegister(12, "r12", false, true);
    public static final PhysicalRegister r13 = new PhysicalRegister(13, "r13", false, true);
    public static final PhysicalRegister r14 = new PhysicalRegister(14, "r14", false, true);
    public static final PhysicalRegister r15 = new PhysicalRegister(15, "r15", false, true);

    public static Set<PhysicalRegister> CallerSavedRegs = new HashSet<>();
    public static Set<PhysicalRegister> CalleeSavedRegs = new HashSet<>();
    public static List<PhysicalRegister> allRegs = new ArrayList<>();
    public static List<PhysicalRegister> FuncParamRegs = new ArrayList<>();
    public static List<PhysicalRegister> UseRegs = new ArrayList<>();

    static {
        allRegs.add(rax);
        allRegs.add(rbx);
        allRegs.add(rcx);
        allRegs.add(rdx);
        allRegs.add(rsi);
        allRegs.add(rdi);
        allRegs.add(rbp);
        allRegs.add(rsp);
        allRegs.add(r8);
        allRegs.add(r9);
        allRegs.add(r10);
        allRegs.add(r11);
        allRegs.add(r12);
        allRegs.add(r13);
        allRegs.add(r14);
        allRegs.add(r15);

        FuncParamRegs.add(rdi);
        FuncParamRegs.add(rsi);
        FuncParamRegs.add(rdx);
        FuncParamRegs.add(rcx);

        //I will not use the following parameters to deliver parameter
        //FuncParamRegs.add(r8);
        //FuncParamRegs.add(r9);

        //caller save registers
        UseRegs.add(r8);
        UseRegs.add(r9);
        UseRegs.add(r10);
        UseRegs.add(r11);

        //may be I will add registers later
        UseRegs.add(r11);
        UseRegs.add(r12);
        UseRegs.add(r13);
        UseRegs.add(r14);

        allRegs.stream().filter(PhysicalRegister::isCallerSave).forEach(CallerSavedRegs::add);
        allRegs.stream().filter(PhysicalRegister::isCalleeSave).forEach(CalleeSavedRegs::add);

    }
}