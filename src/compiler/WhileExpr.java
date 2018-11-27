package compiler;

public class WhileExpr extends Expr {

    private Expr condExpr;
    private Expr doExpr;
    private boolean isNot;

    public WhileExpr(Expr condExpr, Expr doExpr, Type type) {
        this.condExpr = condExpr;
        this.doExpr = doExpr;
        this.isNot = Type.Whilenot == type;
    }

    @Override
    Val eval(Env env) {
        Val val=NilVal.nil;
        while (condExpr.eval(env).checkBool() != isNot) {
            val=doExpr.eval(env);
        }
        return val;
    }
}
