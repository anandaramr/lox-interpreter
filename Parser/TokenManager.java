package Parser;

import Lexer.Token;
import Lexer.TokenType;

import java.util.ArrayList;
import java.util.List;

public class TokenManager {
    List<Token> tokens;
    private int current;

    TokenManager(List<Token> tokens) {
        this.tokens = tokens;
        this.current = 0;
    }

    public boolean match(TokenType...types) {
        if(isAtEnd()) return false;

        for (TokenType type: types) {
            if(peek().type==type) {
                advance();
                return true;
            }
        }
        return false;
    }

    public void expect(String err, TokenType ...type) {
        if(match(type)) return;
        synchronize();
        throw new Parser().error("Syntax error");
    }

    public Token peek() {
        if(isAtEnd()) return null;
        return tokens.get(current);
    }

    public void synchronize() {
        if(previous().type==TokenType.SEMICOLON || match(TokenType.SEMICOLON)) return;

        while(!match(TokenType.SEMICOLON)) advance();
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

    public List<Token> getTokens() {
        return new ArrayList<>(tokens);
    }
}