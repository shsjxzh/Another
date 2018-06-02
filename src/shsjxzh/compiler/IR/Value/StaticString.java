package shsjxzh.compiler.IR.Value;

import shsjxzh.compiler.IR.IRVisitor;

public class StaticString extends StaticData{
    String data;

    public StaticString(String data, int num) {
        super("str" + num);
        this.data = data;
    }

    public String getData() {
        return data;
    }

    @Override
    public void accept(IRVisitor visitor) {
        visitor.visit(this);
    }
}
