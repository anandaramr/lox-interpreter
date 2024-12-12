package Lexer;

import Lox.Lox;

public class SourceManager {
    private final String source;
    private int start;
    private int current;
    public final int line;

    SourceManager(String source, int line) {
        this.source = source;
        this.start = 0;
        this.current = 0;
        this.line = line;
    }

    public char peek() {
        if(isAtEnd()) return '\0';

        return source.charAt(current);
    }

    public boolean match(char ...characters) {
        if(isAtEnd()) return false;

        for(char character: characters) {
            if(peek()==character) {
                advance();
                return true;
            }
        }
        return false;
    }

    public void advance() {
        if(!isAtEnd()) current++;
    }

    public boolean isAtEnd() {
        return current==source.length();
    }

    public void expect(char c, String err) {
        if(isAtEnd() && c!='"') {
            Lox.error("Unexpected end of statement");
            return;
        }

        if(!match(c)) {
            Lox.error("Syntax Error: Expected '" + c + "'");
            advance();
        }
    }

    public String getLexeme() {
        return source.substring(start,current);
    }

    public void resetPtr() {
        start = current;
    }
}