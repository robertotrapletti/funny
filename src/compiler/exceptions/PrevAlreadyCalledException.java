package compiler.exceptions;

import java.io.IOException;

public class PrevAlreadyCalledException extends IOException {
    public PrevAlreadyCalledException() {
        super("Tokenizer.prev() alreay called");
    }
}
