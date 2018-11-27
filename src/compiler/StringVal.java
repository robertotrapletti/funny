package compiler;

public class StringVal extends Val {
    private String string;

    public StringVal(String string) {
        this.string = string;
    }

    public String toString(){
        return string;
    }
}
