package compiler;

import compiler.exceptions.SyntaxErrorException;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.StringReader;
import java.util.ArrayList;

public class CompilerTest {

    @Test
    public void compilerTest() throws Exception, SyntaxErrorException {
        Compiler compiler=new Compiler(new StringReader("{ciao ->print(ciao);}"));
        Expr expr=compiler.program();
    }

    @Test
    public void compilerFromFileTest() throws Exception, SyntaxErrorException {

        Compiler comp = new Compiler(new BufferedReader(new FileReader(new File("/Users/deca/Desktop/repo_comp/funny/test/resources/test.funny"))));
        Expr expr=comp.program();
        new InvokeExpr(expr, new ArrayList<>()).eval(null);
    }

}
