package compiler.exceptions;

public class DuplicateException extends RuntimeException {
    public DuplicateException(String found_duplicate) {
        super(found_duplicate);
    }
}
