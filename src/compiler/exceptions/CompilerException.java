package compiler.exceptions;

public class CompilerException extends RuntimeException {
    public CompilerException(String message){
        super("Compiler exception: "+message);
    }
}
