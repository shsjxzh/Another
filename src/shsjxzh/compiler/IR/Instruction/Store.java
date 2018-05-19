package shsjxzh.compiler.IR.Instruction;

import shsjxzh.compiler.IR.BasicBlock;
import shsjxzh.compiler.IR.Instruction.*;

public class Store extends Instruction {
    public Store(BasicBlock curBB) {
        super(curBB);
    }
}
