package shsjxzh.compiler.IR.Value;

import shsjxzh.compiler.IR.IRVisitor;

public class StaticString extends StaticData{
    String data;

    public StaticString(String data) {
        super("str");
        this.data = data;
    }

    @Override
    public void accept(IRVisitor visitor) {
        visitor.visit(this);
    }
}
