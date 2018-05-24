package shsjxzh.compiler.IR.Value;

import shsjxzh.compiler.IR.IRVisitor;

public class VirtualRegister extends Register {
    public VirtualRegister(String name) {
        super(name);
    }
    @Override
    public void accept(IRVisitor visitor) {
        visitor.visit(this);
    }
}
