package compiler;

public class BoolVal extends Val{

    private final static BoolVal _true= new BoolVal(true);
    private final static BoolVal _false= new BoolVal(false);
    private boolean bool;

    private BoolVal(boolean bool) {
        this.bool = bool;
    }

    static BoolVal val(Type b){return b==Type.True?_true:_false;}
    static BoolVal val(boolean b){return b?_true:_false;}

    public Val equal(Val rightVal) {
        return BoolVal.val(this.bool==rightVal.checkBool());
    }
    public Val notEqual(Val rightVal) {
        return BoolVal.val(!(this.bool==rightVal.checkBool()));
    }

    public Val or(Val rightVal) {
        return BoolVal.val(this.bool||rightVal.checkBool());
    }

    public Val and(Val rightVal) {
        return BoolVal.val(this.bool&&rightVal.checkBool());
    }

    boolean checkBool(){
        return bool;
    }

    public String toString(){
        return String.valueOf(bool);
    }

}
