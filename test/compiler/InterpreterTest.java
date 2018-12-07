package compiler;

import compiler.exceptions.SyntaxErrorException;
import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class InterpreterTest {

    @Test
    public void printTest() throws Exception, SyntaxErrorException {
        Compiler compiler=new Compiler(new StringReader("{isOdd isEven ->\n" +
                "\tisOdd = {(n) -> if n == 0 then false else isEven(n - 1) fi};\n" +
                "\tisEven = {(n) -> if n == 0 then true else isOdd(n - 1) fi};\n" +
                "\n" +
                "\tprintln(isEven(1002));\n" +
                "}"));
        Expr expr=compiler.program();
        assertTrue(new InvokeExpr(expr, new ArrayList<>()).eval(null).checkBool());
    }
}
