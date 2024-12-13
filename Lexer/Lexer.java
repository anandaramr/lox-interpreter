package Lexer;

import Lox.Lox;

import java.util.ArrayList;
import java.util.List;

public class Lexer {
    private final List<Token> tokens;
    SourceManager src;

    public Lexer() {
        this.tokens = new ArrayList<>();
    }

    public List<Token> tokenize(String source) {
        src = new SourceManager(source);

        while(!src.isAtEnd()) {
            if(isAlpha(src.peek())) {
                while(!src.isAtEnd() && isAlphaNumeric(src.peek())) src.advance();

                String lexeme = src.getLexeme();
                TokenType type = Keywords.getType(lexeme);

                if(type==null) type = TokenType.IDENTIFIER;
                addToken(type, lexeme);
            }
            else if(isNumeric(src.peek())) {
                while(!src.isAtEnd() && isNumeric(src.peek())) src.advance();
                addToken(TokenType.INTEGER, src.getLexeme());
            }
            else if (src.match('+')) {
                if(!src.match('=')) src.match('+');

                String lexeme = src.getLexeme();
                addToken(Operators.getType(lexeme), lexeme);
            }
            else if (src.match('-')) {
                if(!src.match('=')) src.match('-');

                String lexeme = src.getLexeme();
                addToken(Operators.getType(lexeme), lexeme);
            }
            else if ( src.match('*', '/', '=', '<', '>') ) {
                src.match('=');

                String lexeme = src.getLexeme();
                addToken(Operators.getType(lexeme), lexeme);
            }
            else if (src.match('"')) {
                src.resetPtr();
                while(!src.isAtEnd() && src.peek()!='"') src.advance();
                addToken(TokenType.STRING, src.getLexeme());

                src.expect('"', "Expected \" end of string");
            }
            else if (src.match('(')) {
                addToken(TokenType.LEFTPAR, src.getLexeme());
            }
            else if (src.match(')')) {
                addToken(TokenType.RIGHTPAR, src.getLexeme());
            }
            else if (src.match(';')) {
                addToken(TokenType.SEMICOLON, src.getLexeme());
            }
            else if (src.match('\n')) {
                src.line++;
            } else if (!src.match(' ', '\t')) {
                // to-do: define SyntaxError class to handle error
                Lox.error("Syntax Error: Unexpected token: \"" + src.peek() + '"', src.line);
                src.advance();
            }

            src.resetPtr();
        }

        return tokens;
    }

    private void addToken(TokenType type, String lexeme) {
        tokens.add(new Token(type, lexeme, src.line));
    }

    private boolean isAlpha(char c) {
        return ( c>='a' && c<='z' || c>='A' && c<='Z' );
    }

    private boolean isNumeric(char c) {
        return c>='0' && c<='9';
    }

    private boolean isAlphaNumeric(char c) {
        return isAlpha(c) || isNumeric(c);
    }
}

