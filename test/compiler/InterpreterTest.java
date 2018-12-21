package compiler;

import compiler.exceptions.SyntaxErrorException;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class InterpreterTest {

    @Test
    public void test1() throws Exception, SyntaxErrorException {
        Compiler cmp = new Compiler(new BufferedReader(new FileReader(new File("/Users/robertotrapletti/Google Drive/Scuola/SUPSI/I4P/Compilatori e interpreti/code/funny/out/test/funny/program.funny"))));
        Expr expr=cmp.program();
        new InvokeExpr(expr, new ArrayList<>()).eval(null);
    }

    @Test
    public void trueTest() throws Exception, SyntaxErrorException {
        Compiler compiler=new Compiler(new StringReader("{isOdd isEven ->\n" +
                "\tisOdd = {(n) -> if n == 0 then false else isEven(n - 1) fi};\n" +
                "\tisEven = {(n) -> if n == 0 then true else isOdd(n - 1) fi};\n" +
                "\n" +
                "\tisEven(1002);\n" +
                "}"));
        Expr expr=compiler.program();
        assertTrue(new InvokeExpr(expr, new ArrayList<>()).eval(null).checkBool());
    }
    @Test
    public void falseTest() throws Exception, SyntaxErrorException {
        Compiler compiler=new Compiler(new StringReader("{isOdd isEven ->\n" +
                "\tisOdd = {(n) -> if n == 0 then false else isEven(n - 1) fi};\n" +
                "\tisEven = {(n) -> if n == 0 then true else isOdd(n - 1) fi};\n" +
                "\n" +
                "\tisEven(1001);\n" +
                "}"));
        Expr expr=compiler.program();
        assertFalse(new InvokeExpr(expr, new ArrayList<>()).eval(null).checkBool());
    }

    @Test
    public void stringTest() throws Exception, SyntaxErrorException {
        Compiler c=new Compiler(new StringReader("{ ->\n" +
                "\tprint(\"Hello, world!\\n\");\n" +
                "\tprint(\"Hi!\", \"\\n\");\n" +
                "\tprintln(\"你好\");\n" +
                "}"));
        assertEquals("你好",new InvokeExpr(c.program(),new ArrayList<>()).eval(null).toString());
    }

    @Test
    public void averageTest() throws Exception, SyntaxErrorException {
        Compiler c=new Compiler(new StringReader("{average sqr abs sqrt x ->\n" +
                "    average = {(x y) -> (x + y) / 2};\n" +
                "    sqr = {(x) -> x * x};\n" +
                "    abs = {(x) -> if x >= 0 then x else -x fi};\n" +
                "    sqrt = {(x) tolerance isGoodEnough improve sqrtIter ->\n" +
                "        tolerance = 1e-30;\n" +
                "\n" +
                "        isGoodEnough = {(guess) -> abs(sqr(guess) - x) < tolerance};\n" +
                "        improve = {(guess) -> average(guess, x / guess)};\n" +
                "        sqrtIter = {(guess) ->\n" +
                "            if isGoodEnough(guess) then guess else sqrtIter(improve(guess)) fi\n" +
                "        };\n" +
                "        sqrtIter(1)\n" +
                "    };\n" +
                "\n" +
                "    x = 16;\n" +
                "    println(\"sqrt(\", x, \"): \", sqrt(x)==4);\n" +
                "}"));
        assertEquals("true",new InvokeExpr(c.program(),new ArrayList<>()).eval(null).toString());
    }

    @Test
    public void numTets() throws Exception, SyntaxErrorException {
        Compiler c= new Compiler(new StringReader("{a sqr x ->\n" +
                "\n" +
                "\tsqr = {(x) -> x * x};\n" +
                "\tx = {(z) -> sqr};\n" +
                "\n" +
                "\tprintln(x(2)(3));\n" +
                "\n" +
                "    println(10 / 3);\n" +
                "    println(20 / 3);\n" +
                "\n" +
                "\tprintln({(x)->{() -> x}}(4)());\n" +
                "\n" +
                "    a = 1024 * 1024 * 1024 * 1024 * 1024 * 1024 * 1024 * 1024 * 1024;\n" +
                "    println(a);\n" +
                "    println(1 / a);\n" +
                "    println(1 / a * a);\n" +
                "    println(1 / a * a == 1);\n" +
                "\n" +
                "    println(3.27 % .7);\n" +
                "}"));
        new InvokeExpr(c.program(),new ArrayList<>()).eval(null);
    }

    @Test
    public void orAndTest() throws Exception, SyntaxErrorException {
        Compiler c=new Compiler(new StringReader("\n" +
                "{p q->\n" +
                "    q=3;\n" +
                "    if p == nil then\n" +
                "      println(\"È nil!!\");else println(\"Non è nil...\");\n" +
                "    fi;\n" +
                "if q == nil then\n" +
                "println(\"È nil!!\");else println(\"Non è nil...\");\n" +
                "fi\n"+
                "}"));
       new InvokeExpr(c.program(),new ArrayList<>()).eval(null);
    }

    @Test
    public void concTest() throws Exception, SyntaxErrorException {
        Compiler c=new Compiler(new StringReader("{average  ->\n" +
                "        average = {(x y) -> (x + y) / 2};\n" +
                "        println(average+\" \"+nil);\n" +
                "        }"));
        new InvokeExpr(c.program(),new ArrayList<>()).eval(null);
    }

}
