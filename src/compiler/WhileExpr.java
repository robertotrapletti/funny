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
        while (condExpr.eval(env).checkBool() != isNot) {
            doExpr.eval(env);
        }
        return NilVal.nil;
    }
}
