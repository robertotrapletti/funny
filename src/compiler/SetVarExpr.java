package compiler;

import com.sun.tools.corba.se.idl.constExpr.Equal;
import compiler.exceptions.InterpreterException;

public class SetVarExpr extends Expr {
    private String id;
    private Type op;
    private Expr expr;

    public SetVarExpr(String id, Type op, Expr expr) {
        this.id = id;
        this.op = op;
        this.expr = expr;
    }

    @Override
    Val eval(Env env) {
        Val val=expr.eval(env);
        switch (op){
            case EQUAL:return env.setVal(id,val);
            case PLUS_EQUAL:return env.setVal(id,env.getVal(id).plus(val));
            case MINUS_EQUAL:return env.setVal(id,env.getVal(id).minus(val));
            case MODULO_EQUAL:return env.setVal(id,env.getVal(id).mod(val));
            case MULTIPLICATION_EQUAL:return env.setVal(id,env.getVal(id).mult(val));
            case DIVISION_EQUAL:return env.setVal(id,env.getVal(id).div(val));
            default: throw new InterpreterException("op not valid "+op.toString());
        }
    }
}
