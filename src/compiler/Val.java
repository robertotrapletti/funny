package compiler;

import compiler.exceptions.InterpreterException;

import java.math.BigDecimal;

abstract class Val extends Expr {

    @Override
    Val eval(Env env) {
        return this;
    }

    public Val or(Val rightVal) {
        throw new InterpreterException("operation not permitted");
    }

    public Val and(Val rightVal) {
        throw new InterpreterException("operation not permitted");
    }

    public Val equal(Val rightVal) {
        throw new InterpreterException("operation not permitted");
    }

    public Val notEqual(Val rightVal) {
        throw new InterpreterException("operation not permitted");
    }

    public Val plus(Val addString){return new StringVal(this.toString()+addString.toString());}

    public Val lt(Val rightVal) {
        throw new InterpreterException("operation not permitted");
    }

    public Val le(Val rightVal) {
        throw new InterpreterException("operation not permitted");
    }

    public Val gt(Val rightVal) {
        throw new InterpreterException("operation not permitted");
    }

    public Val ge(Val rightVal) {
        throw new InterpreterException("operation not permitted");
    }


    public Val minus(Val rightVal) {
        throw new InterpreterException("operation not permitted");
    }

    public Val mod(Val rightVal) {
        throw new InterpreterException("operation not permitted");
    }

    public Val div(Val rightVal) {
        throw new InterpreterException("operation not permitted");
    }

    public Val mult(Val rightVal) {
        throw new InterpreterException("operation not permitted");
    }

    ClosureVal checkClosure() {
        throw new InterpreterException("operation not permitted");
    }

    boolean checkBool() {
        throw new InterpreterException("operation not permitted");
    }

    protected BigDecimal checkNum() {
        throw new InterpreterException("operation not permitted");
    }
}
