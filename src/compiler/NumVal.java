package compiler;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class NumVal extends Val{

    private BigDecimal num;

    public NumVal(BigDecimal num) {
        this.num = num;
    }

    public Val minus(Val rightVal) {
        return new NumVal(num.subtract(rightVal.checkNum()));
    }
    public Val plus(Val rightVal) {
        return new NumVal(num.add(rightVal.checkNum()));
    }
    public Val mult(Val rightVal) {
        return new NumVal(num.multiply(rightVal.checkNum()));
    }
    public Val div(Val rightVal) {
        return new NumVal(num.divide(rightVal.checkNum(),6, RoundingMode.CEILING));
    }
    public Val mod(Val rightVal) {
        return new NumVal(num.remainder(rightVal.checkNum()));
    }
    public Val equal(Val val){
        return new BoolVal(num.equals(val.checkNum()));
    }

    protected BigDecimal checkNum() {
        return num;
    }

    public String toString(){
        return num.toString();
    }

}
