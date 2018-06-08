package shsjxzh.compiler.BackEnd;

import shsjxzh.compiler.IR.BasicBlock;
import shsjxzh.compiler.IR.Function;
import shsjxzh.compiler.IR.IRRoot;
import shsjxzh.compiler.IR.Instruction.*;
import shsjxzh.compiler.IR.Instruction.Instruction;
import shsjxzh.compiler.IR.Value.VirtualRegister;

import java.util.HashSet;
import java.util.Set;

public class LivenessAnalyzer {
    //Thanks for wu yu wei 's tutorial
    IRRoot irRoot;
    private Set<String> BBVisit = new HashSet<>();

    boolean continueFlag = true;
    Set<VirtualRegister> tmpIn = new HashSet<>();
    Set<VirtualRegister> tmpOut = new HashSet<>();

    public LivenessAnalyzer(IRRoot irRoot) {
        this.irRoot = irRoot;
    }

    private void FunProcess(Function node){
        if (node.isBuildIn()) return;

        continueFlag = true;
        while(continueFlag) {
            continueFlag = false;
            BBVisit.clear();
            BBProcess(node.getEndBB());
        }
    }

    private void BBProcess(BasicBlock node){
        if (node == null || BBVisit.contains(node.getName())) return;
        BBVisit.add(node.getName());

        Instruction ins;
        for (ins = node.getLastIns(); ins != null; ins = ins.Prev() ){
            tmpIn.clear();
            tmpOut.clear();
            tmpIn.addAll(ins.liveIn);
            tmpOut.addAll(ins.liveOut);
            ins.liveIn.clear();
            ins.liveOut.clear();

            if(ins instanceof Branch){
                BasicBlock thenBB = ins.getBelongBB().succssorBBMap.get(((Branch) ins).getThenLabel());
                BasicBlock otherWiseBB = ins.getBelongBB().succssorBBMap.get(((Branch) ins).getOtherwiseLabel());

                if(thenBB.getHeadIns() != null)
                    ins.liveOut.addAll(thenBB.getHeadIns().liveIn);
                if(otherWiseBB.getHeadIns() != null)
                    ins.liveOut.addAll(otherWiseBB.getHeadIns().liveIn);
            }
            else if(ins instanceof Jump){
                BasicBlock jmpBB = ins.getBelongBB().succssorBBMap.get(((Jump) ins).getNextLabel());
                if(jmpBB.getHeadIns() != null)
                    ins.liveOut.addAll(jmpBB.getHeadIns().liveIn);
            }
            else if(!(ins instanceof Return)) {
                assert ins.Next() != null;
                ins.liveOut.addAll(ins.Next().liveIn);
            }

            ins.liveIn.addAll(ins.use);

            ins.liveIn.addAll(ins.liveOut);
            VirtualRegister defRegister = ins.def;
            if(!ins.use.contains(defRegister)){
                ins.liveIn.remove(defRegister);
            }

            if(!ins.liveIn.equals(tmpIn) || !ins.liveOut.equals(tmpOut)){
                continueFlag = true;
            }
        }
        node.predecessorBBMap.values().forEach(this::BBProcess);
    }

    public void run(){
        irRoot.functionMap.values().forEach(this::FunProcess);
    }
}
