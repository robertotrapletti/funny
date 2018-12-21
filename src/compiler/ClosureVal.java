package compiler;

import java.util.List;

public class ClosureVal extends Val {
    private final Env env;
    private final FunExpr funExpr;

    ClosureVal(Env env, FunExpr funExpr) {
        this.env = env;
        this.funExpr = funExpr;
    }

    Val apply(List<Val> argVals) {
        return funExpr.getCode().eval(
                new Env(env,new Frame(funExpr.getParams(), funExpr.getLocals(), argVals)));
    }

    ClosureVal checkClosure(){
        return this;
    }

}
