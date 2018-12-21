package compiler;

import compiler.exceptions.InterpreterException;

import java.math.BigDecimal;

public class UnaryExpr extends Expr {

    private Type sign;
    private Expr expr;

    public UnaryExpr(Type sign, Expr expr) {
        this.sign = sign;
        this.expr = expr;
    }

    @Override
    Val eval(Env env) {
        Val val = expr.eval(env);
        switch (sign) {
            case PLUS:
                return val;
            case MINUS:
                return new NumVal(BigDecimal.ZERO).minus(val);
            case BANG:
                return BoolVal.val(!val.checkBool());
            default:
                throw new InterpreterException("cannot understand sign: "+sign.toString());

        }
    }
}
