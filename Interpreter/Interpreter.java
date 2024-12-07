package Interpreter;

import Lexer.Token;
import Lexer.TokenType;
import java.util.List;

public class Interpreter {
    private TokenManager tokens;
    
    public void execute(List<Token> tokenList) {
        tokens = new TokenManager(tokenList);

        while(!tokens.isAtEnd()) {

            if(tokens.match(TokenType.SAY)) {
                StringBuilder buffer = parseExpression();
                if(!buffer.isEmpty()) System.out.println(buffer);
            }
            else if(tokens.match(TokenType.IDENTIFIER)) {
                String variable = tokens.previous().lexeme;
                StringBuilder buffer = new StringBuilder();

                if(tokens.match(TokenType.PLUSEQ)) {
                    buffer.append(Pool.getVariable(variable));
                } else if(!tokens.match(TokenType.EQUAL)) continue;

                buffer.append(parseExpression());
                Pool.setVariable(variable, buffer.toString());
            }
            else if (tokens.match(TokenType.SEMICOLON)) {
                tokens.advance();
            }
            else {
                System.out.println("Unexpected symbol: \n\t" + tokens.peek().lexeme);
                break;
            }
        }
    }

    private StringBuilder parseExpression() {
        StringBuilder buffer = new StringBuilder();
        if(tokens.match(TokenType.STRING, TokenType.IDENTIFIER)) buffer.append(valueOf(tokens.previous()));

        while(tokens.match(TokenType.PLUS)) {
            tokens.expect("Unexpected symbol: \n\t" + tokens.peek().lexeme, TokenType.STRING, TokenType.IDENTIFIER);
            buffer.append(valueOf(tokens.previous()));
        }

        boolean noError = tokens.expect("Syntax Error: \n\tExpected semicolon at end of input", TokenType.SEMICOLON);
        return noError ? buffer : new StringBuilder();
    }

    private String valueOf(Token token) {
        switch (token.type) {
            case STRING -> {
                return token.lexeme;
            }
            case IDENTIFIER -> {
                String value = Pool.getVariable(token.lexeme);
                
                if(value==null) {
                    System.out.println("Undefined variable: \n\t" + token.lexeme);
                    tokens.synchronize();
                    return "";
                }
                return value;
            }
            default -> {
                System.out.println("Unexpected token: \n\t" + token.lexeme);
                tokens.synchronize();
                return "";
            }
        }
    }
}

class TokenManager {
    List<Token> tokens;
    private int current;

    TokenManager(List<Token> tokens) {
        this.tokens = tokens;
    }

    public boolean match(TokenType ...types) {
        if(isAtEnd()) return false;

        for (TokenType type: types) {
            if(peek().type==type) {
                advance();
                return true;
            }
        }
        return false;
    }

    public boolean expect(String err, TokenType ...type) {
        if(match(type)) return true;

        System.out.println(err);
        synchronize();
        return false;
    }

    public Token peek() {
        if(isAtEnd()) return null;
        return tokens.get(current);
    }

    public void synchronize() {
        while(!isAtEnd() && peek().type!=TokenType.SEMICOLON) advance();
    }

    public Token previous() {
        return tokens.get(current-1);
    }

    public void advance() {
        if(!isAtEnd()) current++;
    }

    public boolean isAtEnd() {
        return current==tokens.size();
    }
}