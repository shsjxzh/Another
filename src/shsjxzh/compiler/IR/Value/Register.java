package shsjxzh.compiler.IR.Value;

public abstract class Register extends Value {
    //a general place where data will be stored temporarily
    protected String name;

    public Register(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Register && name.equals(((Register) obj).getName());
    }
}
