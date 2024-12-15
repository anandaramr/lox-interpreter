package Ngi;

import Lexer.Token;

public class RuntimeError extends RuntimeException {
    final Token token;

    public RuntimeError(Token token, String err) {
        super(err);
        this.token = token;
    }
}