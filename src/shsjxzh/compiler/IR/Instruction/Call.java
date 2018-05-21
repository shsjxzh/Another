package shsjxzh.compiler.IR.Instruction;

import shsjxzh.compiler.IR.BasicBlock;
import shsjxzh.compiler.IR.Function;
import shsjxzh.compiler.IR.IRVisitor;
import shsjxzh.compiler.IR.Value.Register;
import shsjxzh.compiler.IR.Value.Value;

import java.util.List;

public class Call extends Instruction {
    private Function callFunc;
    private List<Value> argvs;
    private Register dest;

    public Call(BasicBlock curBB, Function callFunc, List<Value> argvs, Register dest) {
        super(curBB);
        this.callFunc = callFunc;
        this.argvs = argvs;
        this.dest = dest;
    }

    @Override
    public void accept(IRVisitor visitor) {
        visitor.visit(this);
    }
}
