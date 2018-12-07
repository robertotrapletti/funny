package compiler;

public class StringVal extends Val {
    private String string;

    public StringVal(String string) {
        this.string = string;
    }

    public StringVal plus(StringVal addString){return new StringVal(this.string+addString);}

    public String toString(){
        return string;
    }
}
