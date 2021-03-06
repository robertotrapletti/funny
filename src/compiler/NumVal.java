package compiler;

import java.math.BigDecimal;

public class NumVal extends Val{

    private BigDecimal num;

    public NumVal(BigDecimal num) {
        this.num = num;
    }

    public Val minus(Val rightVal) {
        return new NumVal(num.subtract(rightVal.checkNum()));
    }
    protected BigDecimal checkNum() {
        return num;
    }

    public String toString(){
        return num.toString();
    }

}
