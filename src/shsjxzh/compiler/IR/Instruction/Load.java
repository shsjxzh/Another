package shsjxzh.compiler.IR.Instruction;

import shsjxzh.compiler.IR.BasicBlock;
import shsjxzh.compiler.IR.IRVisitor;
import shsjxzh.compiler.IR.Value.Register;
import shsjxzh.compiler.IR.Value.Value;

public class Load extends Instruction {
    private Register dest;

    //place = [ reg + reg*scale + number ]
    private Register base;
    private Register index;
    private int scale;
    private Value displacement;

    //how many bytes will I take
    /*
    private int ByteSize;

    public Load(BasicBlock curBB, Register dest, int byteSize) {
        super(curBB);
        this.dest = dest;
        ByteSize = byteSize;
    }*/

    public Load(BasicBlock belongBB, Register dest, Register base, Register index, int scale, Value displacement) {
        super(belongBB);
        this.dest = dest;
        this.base = base;
        this.index = index;
        this.scale = scale;
        this.displacement = displacement;
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
