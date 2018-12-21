package compiler;

public class BoolVal extends Val{

    final static BoolVal _true = new BoolVal(true);
    final static BoolVal _false = new BoolVal(false);
    private boolean bool;

    private BoolVal(boolean bool) {
        this.bool = bool;
    }

    public static BoolVal val(boolean b){return b?_true:_false;}
    public static BoolVal val(Type b){return b==Type.True ?_true:_false;}

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
