package compiler;

public class GetVarExpr extends Expr {
    private String id;

    public GetVarExpr(String id) {
        this.id = id;
    }

    @Override
    Val eval(Env env) {
        return env.getVal(id);
    }
}
