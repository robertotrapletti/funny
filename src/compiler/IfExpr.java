package compiler;

public class IfExpr extends Expr {

    private Expr condExpr;
    private Expr thenExpr;
    private Expr elseExpr;
    private boolean isNot;

    public IfExpr(Expr condExpr, Expr thenExpr, Expr elseExpr, Type type) {
        this.condExpr = condExpr;
        this.thenExpr = thenExpr;
        this.elseExpr = elseExpr;
        this.isNot = Type.Ifnot == type;
    }

    @Override
    Val eval(Env env) {

        if (condExpr.eval(env).checkBool()) {
            return isNot ? elseExpr.eval(env) : thenExpr.eval(env);
        } else {
            return !isNot ? elseExpr.eval(env) : thenExpr.eval(env);
        }

    }
}
