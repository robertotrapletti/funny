package compiler;

import compiler.exceptions.SyntaxErrorException;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class InterpreterTest {

    @Test
    public void test1() throws Exception, SyntaxErrorException {
        Compiler cmp = new Compiler(new BufferedReader(new FileReader(new File("/Users/robertotrapletti/Google Drive/Scuola/SUPSI/I4P/Compilatori e interpreti/code/funny/out/test/funny/program.funny"))));
        Expr expr=cmp.program();
        new InvokeExpr(expr, new ArrayList<>()).eval(null);
    }

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
