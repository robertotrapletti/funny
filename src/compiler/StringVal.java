package compiler;

public class StringVal extends Val {
    private String string;

    public StringVal(String string) {
        this.string = string;
    }

    public Val equal(Val rightVal) {
        return BoolVal.val(this.string.equals(rightVal.toString()));
    }
    public Val notEqual(Val rightVal) {
        return BoolVal.val(!(this.string.equals(rightVal.toString())));
    }

    public String toString(){
        return string;
    }
}
