package compiler.exceptions;

public class SyntaxErrorException extends Throwable {
    public SyntaxErrorException(String error) {
        super(error);
    }
}
