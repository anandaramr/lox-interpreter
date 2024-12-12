package Parser;

import Lox.Lox;
import Lexer.Token;
import Lexer.TokenType;

import java.util.List;

public class Parser {
    TokenManager tokens;

    private static class ParseError extends RuntimeException {}

    public Expr parse(List<Token> tokenList) {
        this.tokens = new TokenManager(tokenList);

        try {
            return parseExpression();
        } catch (ParseError e) {
            return null;
        }
    }

    private Expr parseExpression() {
        return parseEquality();
    }

    private Expr parseEquality() {
        Expr left = parseComparison();

        while(tokens.match(TokenType.EQUAL)) {
            Token operator = tokens.previous();
            Expr right = parseComparison();
            left = new Expr.BinaryExpr(left, operator, right);
        }

        return left;
    }

    private Expr parseComparison() {
        Expr left = parseTerm();

        while(tokens.match(TokenType.DEQUAL, TokenType.LESSERTHAN, TokenType.LESSERTHANEQU, TokenType.GREATERTHAN,
                TokenType.GREATERTHANEQU, TokenType.BANGEQUAL)) {
            Token operator = tokens.previous();
            Expr right = parseTerm();
            left = new Expr.BinaryExpr(left, operator, right);
        }

        return left;
    }

    private Expr parseTerm() {
        Expr left = parseFactor();

        while(tokens.match(TokenType.PLUS, TokenType.MINUS)) {
            Token operator = tokens.previous();
            Expr right = parseFactor();
            left = new Expr.BinaryExpr(left, operator, right);
        }

        return left;
    }

    private Expr parseFactor() {
        Expr left = parseUnary();

        while(tokens.match(TokenType.STAR, TokenType.FORSLASH)) {
            Token operator = tokens.previous();
            Expr right = parseUnary();
            left = new Expr.BinaryExpr(left, operator, right);
        }

        return left;
    }

    private Expr parseUnary() {
        if(tokens.match(TokenType.BANG, TokenType.MINUS)) {
            Token operator = tokens.previous();
            Expr right = parseUnary();
            return new Expr.Unary(operator, right);
        }

        return parsePrimary();
    }

    private Expr parsePrimary() {
        if(tokens.match(TokenType.STRING, TokenType.INTEGER)) return new Expr.Literal(tokens.previous().lexeme);
        if(tokens.match(TokenType.IDENTIFIER)) return new Expr.Literal(tokens.previous().lexeme);
        if(tokens.match(TokenType.TRUE)) return new Expr.Literal(true);
        if(tokens.match(TokenType.FALSE)) return new Expr.Literal(false);
        if(tokens.match(TokenType.NULL)) return new Expr.Literal(null);

        // to-do: parse groupings
        throw error("Parse Error: Not handled token: \n\t " + tokens.peek().type);
    }

    ParseError error(String err) {
        Lox.error(err);
        return new ParseError();
    }
}