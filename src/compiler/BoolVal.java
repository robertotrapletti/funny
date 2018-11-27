package compiler;

public class BoolVal extends Val{

    private boolean bool;

    public BoolVal(Type type) {
        this.bool = type == Type.True;
    }
    public BoolVal(boolean bool) {
        this.bool = bool;
    }

    boolean checkBool(){
        return bool;
    }

}
