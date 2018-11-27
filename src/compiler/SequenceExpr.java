package compiler;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class SequenceExpr extends Expr {

    private List<Expr> assignements;

    public SequenceExpr(List<Expr> assignements) {
        this.assignements = assignements;
    }

    @Override
    Val eval(Env env) {
        LinkedList<Val> vals= assignements.stream()
                .map(assignement -> assignement.eval(env)).collect(Collectors.toCollection(LinkedList::new));
        return vals.getLast();
    }
}
