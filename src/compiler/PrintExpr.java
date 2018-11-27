package compiler;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class PrintExpr extends Expr {

    private boolean isNewLine;
    private List<Expr> args;

    public PrintExpr(List<Expr> args, Type type) {
        this.isNewLine = Type.Println == type;
        this.args = args;
    }

    @Override
    Val eval(Env env) {

        LinkedList<Val> vals = args.stream()
                .map(arg -> arg.eval(env)).collect(Collectors.toCollection(LinkedList::new));
        vals.forEach(System.out::print);
        if (isNewLine)
            System.out.println();
        return vals.getLast();

    }
}
