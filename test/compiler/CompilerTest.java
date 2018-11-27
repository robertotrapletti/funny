package compiler;

import compiler.exceptions.SyntaxErrorException;
import org.junit.jupiter.api.Test;

import java.io.StringReader;

public class CompilerTest {

    @Test
    public void compilerTest() throws Exception, SyntaxErrorException {
        Compiler compiler=new Compiler(new StringReader("{ciao ->print(ciao);}"));
        Expr expr=compiler.program();
    }
}
