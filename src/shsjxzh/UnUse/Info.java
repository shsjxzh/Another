package shsjxzh.UnUse;

import shsjxzh.compiler.IR.Value.Value;

public class Info {
    //the return type for constructing the IR
    public Value intValue = null;
    public Value address = null;
    public Value offset = null;

    public Info(Value address, Value offset) {
        this.address = address;
        this.offset = offset;
    }

    public Info(Value intValue) {
        this.intValue = intValue;
    }
}
