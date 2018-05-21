package shsjxzh.compiler.IR.Instruction;

import shsjxzh.compiler.IR.BasicBlock;
import shsjxzh.compiler.IR.IRVisitor;
import shsjxzh.compiler.IR.Value.Register;
import shsjxzh.compiler.IR.Value.Value;

public class Store extends Instruction {
    private Register source;

    //place = [ reg + reg*scale + number ]
    private Register base;
    private Register index;
    private int scale;
    private Value displacement;

    //how many bytes will I take
    private int ByteSize;

    public Store(BasicBlock curBB, Register source, int byteSize) {
        super(curBB);
        this.source = source;
        ByteSize = byteSize;
    }

    public void setBase(Register base) {
        this.base = base;
    }

    public void setIndex(Register index) {
        this.index = index;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public void setDisplacement(Value displacement) {
        this.displacement = displacement;
    }

    @Override
    public void accept(IRVisitor visitor) {
        visitor.visit(this);
    }
    
}
