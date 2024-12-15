package Lexer;

import Ngi.Ngi;

public class SourceManager {
    private final String source;
    private int start;
    private int current;
    public int line;

    SourceManager(String source) {
        this.source = source;
        this.start = 0;
        this.current = 0;
        this.line = 1;
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
            Ngi.error("Unexpected end of statement", line);
            return;
        }

        if(!match(c)) {
            Ngi.error("Syntax Error: Expected '" + c + "'", line);
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