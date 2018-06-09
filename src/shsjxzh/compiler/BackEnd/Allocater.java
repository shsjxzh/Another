package shsjxzh.compiler.BackEnd;

import shsjxzh.compiler.BackEnd.Graph.Graph;
import shsjxzh.compiler.BackEnd.Graph.GraphNode;
import shsjxzh.compiler.IR.BasicBlock;
import shsjxzh.compiler.IR.Function;
import shsjxzh.compiler.IR.IRRoot;
import shsjxzh.compiler.IR.IRVisitor;
import shsjxzh.compiler.IR.Instruction.*;
import shsjxzh.compiler.IR.Value.*;

import java.util.List;
import java.util.Stack;

public class Allocater {
    IRRoot irRoot;

    //final int COLOUR_NUM = 4;

    //private Stack<GraphNode> waitColourNode;
    private List<PhysicalRegister> useRegs = PhysicalRegisterSet.UseRegs;
    private List<PhysicalRegister> paramRegs = PhysicalRegisterSet.FuncParamRegs;

    public void run(){
        irRoot.functionMap.values().forEach(this::allocateFunc);
    }

    private void allocateFunc(Function node){
        for (int i = 0; i < useRegs.size(); ++i) {
            for (VirtualRegister virtualReg : node.funcLocalVarRegs.values()) {
                if (!virtualReg.hasPhysicalReg()){
                    GraphNode tmp = node.interferenceGraph.Vr2NodeMap.get(virtualReg);
                    boolean canAlloc = true;
                    for (GraphNode neighbour : tmp.neighbours) {
                        if (neighbour.colour == i) {
                            canAlloc = false;
                            break;
                        }

                    }
                    if (canAlloc) {
                        tmp.colour = i;
                        virtualReg.trueReg = useRegs.get(i);
                        if (virtualReg.trueReg.isCalleeSave()) node.usedCalleePhyReg.add(virtualReg.trueReg);
                    }
                }
            }
        }

        int i = 0;
        for (VirtualRegister virtualReg : node.funcParams.values()) {
            if (i >= 4) break;
            virtualReg.trueReg = paramRegs.get(i);

            //for callee save register

            if (paramRegs.get(i).isCalleeSave()) {
                node.usedCalleePhyReg.add(paramRegs.get(i));
            }
            ++i;
        }
    }

    public Allocater(IRRoot irRoot) {
        this.irRoot = irRoot;
    }
}
