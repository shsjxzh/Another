package shsjxzh.compiler.BackEnd;

import shsjxzh.compiler.BackEnd.Graph.Graph;
import shsjxzh.compiler.IR.BasicBlock;
import shsjxzh.compiler.IR.Function;
import shsjxzh.compiler.IR.IRRoot;
import shsjxzh.compiler.IR.Instruction.Instruction;
import shsjxzh.compiler.IR.Value.VirtualRegister;

import java.util.HashSet;
import java.util.Set;

public class InterferenceGenerater {
    IRRoot irRoot;
    Graph curGraph;
    private Set<String> BBVisit = new HashSet<>();

    public InterferenceGenerater(IRRoot irRoot) {
        this.irRoot = irRoot;
    }


    private void generateFunc(Function node){
        curGraph = node.interferenceGraph;
        node.funcLocalVarRegs.values().forEach(x -> curGraph.addAllocNode(x));
        node.funcParams.values().forEach(x -> curGraph.addNonAllocNode(x));
        generateBB(node.getStartBB());
    }

    private void generateBB(BasicBlock node){
        if (node == null || BBVisit.contains(node.getName())) return;
        BBVisit.add(node.getName());

        for (Instruction ins = node.getHeadIns(); ins != null; ins = ins.Next()) {
            if (ins.def != null) {
                VirtualRegister def = ins.def;
                ins.liveOut.forEach(x -> curGraph.addEdge(def, x));
            }
        }

        generateBB(node.getAdjacentBB());
        node.succssorBBMap.values().forEach(this::generateBB);
    }


    public void run(){
        irRoot.functionMap.values().forEach(this::generateFunc);
    }
}
