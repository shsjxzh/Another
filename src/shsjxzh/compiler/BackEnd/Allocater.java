package shsjxzh.compiler.BackEnd;

import shsjxzh.compiler.BackEnd.Graph.Graph;
import shsjxzh.compiler.BackEnd.Graph.GraphNode;
import shsjxzh.compiler.IR.BasicBlock;
import shsjxzh.compiler.IR.Function;
import shsjxzh.compiler.IR.IRRoot;
import shsjxzh.compiler.IR.IRVisitor;
import shsjxzh.compiler.IR.Instruction.*;
import shsjxzh.compiler.IR.Value.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class Allocater {
    IRRoot irRoot;

    private int COLOUR_NUM = PhysicalRegisterSet.UseRegs.size();

    private Stack<GraphNode> waitColourNode = new Stack<>();
    private List<PhysicalRegister> useRegs = PhysicalRegisterSet.UseRegs;
    private List<PhysicalRegister> paramRegs = PhysicalRegisterSet.FuncParamRegs;

    public void run(){
        irRoot.functionMap.values().forEach(this::allocateFunc);
    }

    private Graph curGraph;

    private void removeNode(GraphNode node){
        if (node != null) {
            node.degree = -1;
            for (GraphNode neighbour : node.neighbours) {
                if (neighbour.degree >= 0) {
                    --neighbour.degree;
                }
            }
            waitColourNode.push(node);
            curGraph.graphNodeSet.remove(node);
        }
    }

    private void allocateFunc(Function node){
        //spill
        waitColourNode.clear();
        curGraph = node.interferenceGraph;

        GraphNode lastBigNode = null;

        while(!curGraph.graphNodeSet.isEmpty()) {
            while (!curGraph.graphNodeSet.isEmpty()) {
                boolean allBiggerThanColourNum = true;

                List<GraphNode> removeList = new LinkedList<>();

                for (GraphNode graphNode : curGraph.graphNodeSet) {
                    if (graphNode.degree < COLOUR_NUM) {
                        //removeNode(graphNode);
                        removeList.add(graphNode);
                        allBiggerThanColourNum = false;
                    }
                    else{
                        lastBigNode = graphNode;
                    }
                }

                removeList.forEach(x -> removeNode(x));


                if (allBiggerThanColourNum) break;
            }
            if (!curGraph.graphNodeSet.isEmpty()){
                //random remove?
                removeNode(lastBigNode);
            }
        }

        while(!waitColourNode.empty()){
            GraphNode tmp = waitColourNode.pop();
            for (int nowColour = 0; nowColour < COLOUR_NUM; ++nowColour){
                boolean canAlloc = true;
                for (GraphNode neighbour : tmp.neighbours) {
                    if (neighbour.colour == nowColour) {
                        canAlloc = false;
                        break;
                    }
                }
                if (canAlloc){
                    tmp.colour = nowColour;
                    tmp.virReg.trueReg = useRegs.get(nowColour);
                    if (tmp.virReg.trueReg.isCalleeSave()) node.usedCalleePhyReg.add(tmp.virReg.trueReg);
                    break;
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

    /*private void allocateFunc(Function node){
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
    }*/

    public Allocater(IRRoot irRoot) {
        this.irRoot = irRoot;
    }
}
