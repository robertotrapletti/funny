package compiler;

import java.util.ArrayList;
import java.util.List;

public class FunExpr extends Expr{

    private Expr code;
    private List<String> params;
    private List<String> locals;

    public FunExpr(Expr code, List<String> params, List<String> locals) {
        this.code = code;
        this.params = params;
        this.locals = locals;
    }

    public Expr getCode() {
        return code;
    }

    public List<String> getParams() {
        return params;
    }

    public List<String> getLocals() {
        return locals;
    }

    @Override
    Val eval(Env env) {
        return new ClosureVal(env,this);
    }
}
