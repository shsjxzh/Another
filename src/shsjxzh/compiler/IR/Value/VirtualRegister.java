package shsjxzh.compiler.IR.Value;

import shsjxzh.compiler.IR.Function;
import shsjxzh.compiler.IR.IRVisitor;

public class VirtualRegister extends Register {
    public VirtualRegister(String name) {
        super(name);
    }
    @Override
    public void accept(IRVisitor visitor) {
        visitor.visit(this);
    }


    //for stupid code generator
    //public String physicalReg = null;

    public PhysicalRegister trueReg;

    public boolean hasPhysicalReg(){
        return trueReg != null;
    }
}
