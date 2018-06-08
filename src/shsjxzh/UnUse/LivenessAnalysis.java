package shsjxzh.UnUse;

import shsjxzh.compiler.IR.*;
import shsjxzh.compiler.IR.Instruction.*;
import shsjxzh.compiler.IR.Value.*;

import java.util.HashSet;
import java.util.Set;

public class LivenessAnalysis implements IRVisitor {
    private Set<String> BBVisit = new HashSet<>();
    
    private void livenessAnalysis(IR node){
        if (node != null){
            node.accept(this);
        }
    }
    
    @Override
    public void visit(IRRoot node) {
        //the def and use are all
        node.functionMap.values().forEach(x -> livenessAnalysis(x));
    }

    @Override
    public void visit(Function node) {
        if (node.isBuildIn()) return;
        livenessAnalysis(node.getStartBB());
    }

    @Override
    public void visit(BasicBlock node) {
        if (BBVisit.contains(node.getName())) return;
        BBVisit.add(node.getName());

        for (Instruction itr = node.getHeadIns(); itr!= null; itr = itr.Next()){
            livenessAnalysis(itr);
        }

        //try to print
        livenessAnalysis(node.getAdjacentBB());

        node.succssorBBMap.values().forEach(x -> livenessAnalysis(x));
    }
;

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

    }

    @Override
    public void visit(Dec node) {

    }

    @Override
    public void visit(Jump node) {

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
