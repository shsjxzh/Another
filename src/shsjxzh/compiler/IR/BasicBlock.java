package shsjxzh.compiler.IR;

import shsjxzh.compiler.IR.Instruction.Instruction;

import java.util.LinkedHashMap;
import java.util.Map;

public class BasicBlock extends IR{
    private Instruction headIns = null;
    private Instruction lastIns = null;
    //private boolean ended = false;
    private String name;
    //when comes to "if" and so on, "left" means true and "right" means false
    //private BasicBlock leftBB = null;
    //private BasicBlock rightBB = null;
    private Function belongFunc;
    private boolean finish;
    private BasicBlock adjacentBB;

    public Map<String, BasicBlock> predecessorBBMap = new LinkedHashMap<>();
    public Map<String, BasicBlock> succssorBBMap = new LinkedHashMap<>();

    public BasicBlock(String name, Function belongFunc) {
        this.name = name;
        this.belongFunc = belongFunc;
        finish = false;
    }

    public void append(Instruction ins){
        //for debug
        if (finish) throw new RuntimeException("Cannot add instruction to an ended block!");
        if (headIns == null){
            headIns = ins;
            lastIns = ins;
        }
        else {
            lastIns.LinkNext(ins);
            lastIns = ins;
        }
    }

    public void finish(Instruction ins){
        append(ins);
        finish = true;
    }

    public boolean isFinish() {
        return finish;
    }

    public void LinkNextBB(BasicBlock next){
        succssorBBMap.put(next.getName(), next);
        next.predecessorBBMap.put(name, this);
    }

    public BasicBlock getAdjacentBB() {
        return adjacentBB;
    }

    public void setAdjacentBB(BasicBlock adjacentBB) {
        this.adjacentBB = adjacentBB;
    }

    public Instruction getHeadIns() {
        return headIns;
    }

    public Instruction getLastIns() {
        return lastIns;
    }

    public String getName() {
        return name;
    }
    /*
    public BasicBlock getLeftBB() {
        return leftBB;
    }

    public BasicBlock getRightBB() {
        return rightBB;
    }*/

    public Function getBelongFunc() {
        return belongFunc;
    }

    public void accept(IRVisitor visitor){
        visitor.visit(this);
    }
}
