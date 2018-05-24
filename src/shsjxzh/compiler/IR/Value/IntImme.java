package shsjxzh.compiler.IR.Value;

import shsjxzh.compiler.IR.IRVisitor;

public class IntImme extends Value {
    private int value;

    public IntImme(int value) {
        this.value = value;
    }

    public int getImmeValue() {
        return value;
    }

    @Override
    public void accept(IRVisitor visitor) {
        visitor.visit(this);
    }
}
