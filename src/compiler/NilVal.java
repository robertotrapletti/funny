package compiler;

public class NilVal extends Val {
    final static NilVal nil=new NilVal();

    private NilVal(){}

    public String toString(){
        return "nil";
    }

}
