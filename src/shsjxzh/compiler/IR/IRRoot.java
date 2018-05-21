package shsjxzh.compiler.IR;

import shsjxzh.compiler.IR.Instruction.Call;
import shsjxzh.compiler.IR.Instruction.Return;
import shsjxzh.compiler.IR.Value.StaticData;
import shsjxzh.compiler.IR.Value.StaticString;
import shsjxzh.compiler.IR.Value.VirtualRegister;

import java.util.*;

public class IRRoot {
    public Map<String, Function> functionMap = new LinkedHashMap<>();
    //to get rid of redundancy
    public Map<String, StaticString> stringMap = new LinkedHashMap<>();
    public List<StaticData> staticDataList = new ArrayList<>();
    public int regCount = 0;

    //be careful to add sth
    public IRRoot() {
       stringMap.put("\\n", new StaticString("\\n"));

       //for global variable initialize
       //we will simplify it later
       Function init = new Function("__init");
       functionMap.put("__init", init);

       BasicBlock initBB = new BasicBlock("__init");
       //instruction
       VirtualRegister reg = new VirtualRegister(Integer.toString(regCount));
       Call mainCall = new Call(initBB,init, new LinkedList<>(),reg);
       Return mainReturn = new Return(initBB, reg);
       mainCall.LinkNext(mainReturn);

       initBB.setHead(mainCall);
       initBB.setLast(mainReturn);

       init.setStartBB(initBB);
       //??init.setExitBB(initBB);
    }

    public void accept(IRVisitor visitor){
        visitor.visit(this);
    }
}
