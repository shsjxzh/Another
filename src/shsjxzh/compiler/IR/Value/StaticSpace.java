package shsjxzh.compiler.IR.Value;

import shsjxzh.compiler.IR.IRVisitor;

public class StaticSpace extends StaticData {
    int spaceSize; //in Bite

    public StaticSpace(String name, int spaceSize) {
        super(name);
        this.spaceSize = spaceSize;
    }

    @Override
    public void accept(IRVisitor visitor) {
        visitor.visit(this);
    }
}
