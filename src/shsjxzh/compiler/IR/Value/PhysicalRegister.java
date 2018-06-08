package shsjxzh.compiler.IR.Value;

import shsjxzh.compiler.IR.IRVisitor;

public class PhysicalRegister extends Register {
    int num;
    public boolean CalleeSave;
    public boolean CallerSave;

    public PhysicalRegister(int num, String name, boolean callerSave, boolean calleeSave) {
        super(name);
        this.num = num;
        CalleeSave = calleeSave;
        CallerSave = callerSave;
    }

    public boolean isCalleeSave() {
        return CalleeSave;
    }

    public boolean isCallerSave() {
        return CallerSave;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof PhysicalRegister && name.equals(((PhysicalRegister) obj).name);
    }

    @Override
    public void accept(IRVisitor visitor) {
        visitor.visit(this);
    }
}
