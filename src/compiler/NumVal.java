package compiler;

import compiler.exceptions.InterpreterException;

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
        if(rightVal instanceof StringVal)
            return new StringVal(num.toString()+rightVal.toString());
        return new NumVal(num.add(rightVal.checkNum()));
    }

    public Val mult(Val rightVal) {
        return new NumVal(num.multiply(rightVal.checkNum()));
    }
    public Val div(Val rightVal) {
        return new NumVal(num.divide(rightVal.checkNum(),30,RoundingMode.HALF_UP));
    }
    public Val mod(Val rightVal) {
        return new NumVal(num.remainder(rightVal.checkNum()));
    }
    public Val equal(Val val){
        try{
            return BoolVal.val(num.compareTo(val.checkNum())==0);
        }catch (InterpreterException e){
            return BoolVal.val(false);
        }
    }
    public Val notEqual(Val val){
        return BoolVal.val(num.compareTo(val.checkNum())!=0);
    }

    public Val lt(Val rightVal) {
        return BoolVal.val(num.compareTo(rightVal.checkNum()) < 0);
    }

    public Val le(Val rightVal) {
        return BoolVal.val(num.compareTo(rightVal.checkNum()) <= 0);
    }

    public Val gt(Val rightVal) {
        return BoolVal.val(num.compareTo(rightVal.checkNum()) > 0);
    }

    public Val ge(Val rightVal) {
        return BoolVal.val(num.compareTo(rightVal.checkNum()) >= 0);
    }


    protected BigDecimal checkNum() {
        return num;
    }

    public String toString(){
        return num.toString();
    }

}
