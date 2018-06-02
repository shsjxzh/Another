package shsjxzh.compiler.IR;

import shsjxzh.compiler.IR.Instruction.Call;
import shsjxzh.compiler.IR.Instruction.Return;
import shsjxzh.compiler.IR.Value.StaticData;
import shsjxzh.compiler.IR.Value.StaticString;
import shsjxzh.compiler.IR.Value.VirtualRegister;

import java.util.*;

public class IRRoot extends IR{
    public Map<String, Function> functionMap = new LinkedHashMap<>();
    public Map<String, Function> buildInFunctionMap = new LinkedHashMap<>();
    //to get rid of redundancy
    public Map<String, StaticString> stringMap = new LinkedHashMap<>();
    public List<StaticData> staticDataList = new ArrayList<>();
    private int regCount = 0;
    private int BBCount = 0;
    private int strCount = 0;

    //be careful to add sth
    public IRRoot() {
       stringMap.put("\\n", new StaticString("\\n", getStrCountAndIncrease()));

       //for global variable initialize
       //we will simplify it later
       Function init = new Function("__init", "B_" + getBBCountAndIncrease(), 1, 8);
       //init.setReturnSize(8);  //This is the size of int
       functionMap.put("__init", init);
    }

    public int getRegCountAndIncrease(){
        return ++regCount;
    }

    public int getBBCountAndIncrease(){
        return ++BBCount;
    }

    public int getStrCountAndIncrease() {return ++strCount;}

    public void accept(IRVisitor visitor){
        visitor.visit(this);
    }
}
