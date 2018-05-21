package shsjxzh.compiler.IR.Value;

import shsjxzh.compiler.IR.IRVisitor;

public class PhysicalRegister extends Register {
    public PhysicalRegister(String name) {
        super(name);
    }

    @Override
    public void accept(IRVisitor visitor) {
        visitor.visit(this);
    }
}
