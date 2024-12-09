package Lexer;

import java.util.ArrayList;
import java.util.List;

public class Lexer {
    private final List<Token> tokens;
    int line;

    public Lexer() {
        this.tokens = new ArrayList<>();
    }

    public List<Token> tokenize(String source, int line) {
        SourceManager src = new SourceManager(source);
        this.line = line;

        while(!src.isAtEnd()) {
            if(isAlpha(src.peek())) {
                while(!src.isAtEnd() && isAlpha(src.peek())) src.advance();

                TokenType type = Keywords.getType(src.getString());
                if(type==null) type = TokenType.IDENTIFIER;
                addToken(type, src.getString());
            }
            else if (src.peek()=='"') {
                src.ignore();
                while(!src.isAtEnd() && src.peek()!='"') src.advance();
                addToken(TokenType.STRING, src.getString());

                src.expect('"', "Expected end of string \"");

            }
            else if (src.peek()=='+') {
                src.advance();
                if(src.peek()=='=') {
                    src.advance();
                    addToken(TokenType.PLUSEQ, "+=");
                }
                else addToken(TokenType.PLUS, "+");
            }
            else if (src.peek()=='(') {
                src.advance();
                addToken(TokenType.LPAR, "(");
            }
            else if (src.peek()==')') {
                src.advance();
                addToken(TokenType.RPAR, ")");
            }
            else if (src.peek()==';') {
                src.advance();
                addToken(TokenType.SEMICOLON, ";");
            }
            else if (src.peek()=='=') {
                src.advance();

                if(src.peek()=='=') {
                    src.advance();
                    addToken(TokenType.DEQ, "==");
                }
                else addToken(TokenType.EQUAL, "=");
            }
            else if (src.peek()==' ') {
                src.ignore();
            }
            else {
                System.out.println("Unrecognized character: " + src.peek() + " (" + (int) src.peek() + ")");
                System.exit(1);
            }

            src.reset();
        }

        return tokens;
    }

    private void addToken(TokenType type, String lexeme) {
        tokens.add(new Token(type, lexeme, line));
    }

    private boolean isAlpha(char c) {
        return ( c>='a' && c<='z' || c>='A' && c<='Z' );
    }
}

class SourceManager {
    private final String source;
    private int start;
    private int current;

    SourceManager(String source) {
        this.source = source;
        this.start = 0;
        this.current = 0;
    }

    public char peek() {
        return source.charAt(current);
    }

    public void advance() {
        if(!isAtEnd()) current++;
    }

    public boolean isAtEnd() {
        return current==source.length();
    }

    public void ignore() {
        start = ++current;
    }

    public void expect(char c, String err) {
        if(!isAtEnd() && peek()==c) {
            ignore();
            return;
        }

        System.out.println("Syntax Error: \n\t" + err);
    }

    public String getString() {
        return source.substring(start,current);
    }

    public void reset() {
        start = current;
    }
}