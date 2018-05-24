package shsjxzh.compiler.IR;

public abstract class IR {
    public abstract void accept(IRVisitor visitor);
}
