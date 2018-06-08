package shsjxzh.compiler.BackEnd;

import shsjxzh.compiler.IR.*;
import shsjxzh.compiler.IR.Instruction.*;
import shsjxzh.compiler.IR.Value.*;

import java.util.HashSet;
import java.util.Set;

public class DefUseGenerater implements IRVisitor {
    private Set<String> BBVisit = new HashSet<>();

    private void defUseGenerate(IR node){
        if (node != null){
            node.accept(this);
        }
    }


    private boolean valid(IR node){
        return node != null && node instanceof VirtualRegister;
    }
    //1, check def and use are right
    //2, check null node!

    @Override
    public void visit(IRRoot node) {
        //the def and use are all
        node.functionMap.values().forEach(x -> defUseGenerate(x));
    }

    @Override
    public void visit(Function node) {
        if (node.isBuildIn()) return;
        defUseGenerate(node.getStartBB());
    }

    @Override
    public void visit(BasicBlock node) {
        if (BBVisit.contains(node.getName())) return;
        BBVisit.add(node.getName());

        for (Instruction itr = node.getHeadIns(); itr!= null; itr = itr.Next()){
            defUseGenerate(itr);
        }

        //try to print
        defUseGenerate(node.getAdjacentBB());

        node.succssorBBMap.values().forEach(x -> defUseGenerate(x));
    }

    @Override
    public void visit(Binary node) {
        if (valid(node.getDest())) {
            node.def = (VirtualRegister) node.getDest();
            node.use.add((VirtualRegister) node.getDest());
        }

        if (valid(node.getRight())){
            node.use.add((VirtualRegister) node.getRight());
        }
    }

    @Override
    public void visit(Unary node) {
        if (valid(node.getDest())) {
            node.def = (VirtualRegister) node.getDest();
            node.use.add((VirtualRegister) node.getDest());
        }
    }

    @Override
    public void visit(IntCompare node) {
        if (valid(node.getDest())) {
            node.def = (VirtualRegister) node.getDest();
        }

        if (valid(node.getLeft())){
            node.use.add((VirtualRegister) node.getLeft());
        }

        if (valid(node.getRight())){
            node.use.add((VirtualRegister) node.getRight());
        }
    }

    @Override
    public void visit(Inc node) {
        if (valid(node.getBody())) {
            node.def = (VirtualRegister) node.getBody();
            node.use.add((VirtualRegister) node.getBody());
        }
    }

    @Override
    public void visit(Dec node) {
        if (valid(node.getBody())) {
            node.def = (VirtualRegister) node.getBody();
            node.use.add((VirtualRegister) node.getBody());
        }
    }

    @Override
    public void visit(Jump node) {
        //do nothing
    }

    @Override
    public void visit(Branch node) {
        if (valid(node.getCond())){
            node.use.add((VirtualRegister) node.getCond());
        }
    }

    @Override
    public void visit(Return node) {
        if (valid(node.getReturnValue())){
            node.use.add((VirtualRegister) node.getReturnValue());
        }
    }

    @Override
    public void visit(Call node) {
        if (valid(node.getDest())){
            node.def = (VirtualRegister) node.getDest();
        }
        for (Value value : node.getArgvs()) {
            if (valid(value)){
                node.use.add((VirtualRegister) value);
            }
        }
    }

    @Override
    public void visit(HeapAllocate node) {
        if (valid(node.getDest())){
            node.def = (VirtualRegister) node.getDest();
        }
        if (valid(node.getSize())){
            node.use.add((VirtualRegister)node.getSize());
        }
    }

    @Override
    public void visit(Move node) {
        if (valid(node.getDest())){
            node.def = (VirtualRegister) node.getDest();
        }
        if (valid(node.getSource())){
            node.use.add((VirtualRegister) node.getSource());
        }
    }

    @Override
    public void visit(Load node) {
        if (valid(node.getDest())){
            node.def = (VirtualRegister) node.getDest();
        }
        if (valid(node.getBase())){
            node.use.add((VirtualRegister) node.getBase());
        }
        if (valid(node.getIndex())){
            node.use.add((VirtualRegister) node.getIndex());
        }
    }

    @Override
    public void visit(Store node) {
        if (valid(node.getSource())){
            node.use.add((VirtualRegister) node.getSource());
        }
        if (valid(node.getBase())){
            node.use.add((VirtualRegister) node.getBase());
        }
        if (valid(node.getIndex())){
            node.use.add((VirtualRegister) node.getIndex());
        }
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
