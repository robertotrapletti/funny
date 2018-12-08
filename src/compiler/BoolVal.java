package compiler;

public class BoolVal extends Val{

    private boolean bool;

    public BoolVal(Type type) {
        this.bool = type == Type.True;
    }
    public BoolVal(boolean bool) {
        this.bool = bool;
    }

    public Val equal(Val rightVal) {
        return new BoolVal(this.bool==rightVal.checkBool());
    }
    public Val notEqual(Val rightVal) {
        return new BoolVal(!(this.bool==rightVal.checkBool()));
    }

    public Val or(Val rightVal) {
        return new BoolVal(this.bool||rightVal.checkBool());
    }

    public Val and(Val rightVal) {
        return new BoolVal(this.bool&&rightVal.checkBool());
    }

    boolean checkBool(){
        return bool;
    }

    public String toString(){
        return String.valueOf(bool);
    }

}
