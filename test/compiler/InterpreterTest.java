package compiler;

import compiler.exceptions.SyntaxErrorException;
import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.util.ArrayList;

public class InterpreterTest {

    @Test
    public void printTest() throws Exception, SyntaxErrorException {
        Compiler compiler=new Compiler(new StringReader("{ciao->ciao=4;print(ciao);}"));
        Expr expr=compiler.program();
        new InvokeExpr(expr, new ArrayList<Expr>(0)).eval(null);
    }
}
