package shsjxzh.compiler.IR.Value;

import shsjxzh.compiler.IR.IRVisitor;

public abstract class Value {
    public abstract void accept(IRVisitor visitor);
}
