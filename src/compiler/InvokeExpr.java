package compiler;

import java.util.List;
import java.util.stream.Collectors;

public class InvokeExpr extends Expr {

    private Expr expr;
    private List<Expr> args;

    public InvokeExpr(Expr expr, List<Expr> args) {
        this.expr = expr;
        this.args = args;
    }

    @Override
    Val eval(Env env) {
        return expr.eval(env).checkClosure().apply(
                args.stream()
                        .map(arg -> arg.eval(env))
                        .collect(Collectors.toList()));
    }
}
