package shsjxzh.compiler.IR.Value;

import shsjxzh.compiler.IR.Value.Register;

public class VirtualRegister extends Register {
    String hintName;

    public VirtualRegister(String hintName) {
        this.hintName = hintName;
    }
}
