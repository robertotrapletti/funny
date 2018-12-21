package compiler;

import compiler.exceptions.InterpreterException;

public class BinaryExpr extends Expr {
    private Expr left,right;
    private Type op;

    public BinaryExpr(Expr left, Type op,Expr right) {
        this.left = left;
        this.right = right;
        this.op=op;
    }

    @Override
    Val eval(Env env) {
        //left to right evaluation
        Val leftVal=left.eval(env);

        if(op == Type.OR){
            return leftVal.checkBool() ? leftVal : leftVal.or(right.eval(env));
        }else if(op == Type.AND){
            return  !leftVal.checkBool() ? leftVal : leftVal.and(right.eval(env));
        }

        Val rightVal=right.eval(env);
        switch (op){

            case EQUALITY:return leftVal.equal(rightVal);
            case INEQUALITY:return leftVal.notEqual(rightVal);
            case LESS_THAN:return leftVal.lt(rightVal);
            case LESS_EQUAL_THAN:return leftVal.le(rightVal);
            case GREATHER_THAN:return leftVal.gt(rightVal);
            case GREATHER_EQUAL_THEN:return leftVal.ge(rightVal);
            case PLUS:return leftVal.plus(rightVal);
            case MINUS:return leftVal.minus(rightVal);
            case MODULO:return leftVal.mod(rightVal);
            case DIVISION:return leftVal.div(rightVal);
            case MULTIPLICATION:return leftVal.mult(rightVal);
            default:throw new InterpreterException("error");
        }
    }
}
