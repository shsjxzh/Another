package shsjxzh.compiler.IR;
import shsjxzh.compiler.IR.Instruction.*;

public interface IRVisitor {
    //string will be handled later
    
    //fundamental
    void visit(Function node);
    void visit(BasicBlock node);
    void visit(IRRoot node);        //with static data and functions
    
    //instruction
    //Arithmetic
    void visit(Binary node);         //Add, Sub, Mul, Div, Mod, Shl, Shr, And(bitwise), Or(bitwise), Xor
    void visit(Unary node);          //Neg, Not(bitwise), Inc, Dec
    void visit(Compare node);        //EQ, NE, GT, GE, LT, LE
    //Control
    void visit(Jump node);
    void visit(Branch node);
    void visit(Return node);
    //Other
    void visit(Call node);
    void visit(HeapAllocate node);
    
    
    //Value
    void visit(IntImme node);
    void visit(VirtualRegister node);
    void visit(PhysicalRegister node);
    void visit(StaticSpace node);        //static space will be given a register later
    void visit(StaticString node);
}
