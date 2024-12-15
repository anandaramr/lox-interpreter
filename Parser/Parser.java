package Parser;

import Lox.Lox;
import Lexer.Token;
import Lexer.TokenType;

import java.util.ArrayList;
import java.util.List;

public class Parser {
    TokenManager tokens;

    private static class ParseError extends RuntimeException {}

    public List<Stmt> parse(List<Token> tokenList) {
        this.tokens = new TokenManager(tokenList);
        List<Stmt> statements = new ArrayList<>();

        try {
            while (!tokens.isAtEnd()) {
                statements.add(declaration());
            }
            return statements;

        } catch (ParseError e) {
            return null;
        }
    }

    private Stmt declaration() {
        try{
            if(tokens.match(TokenType.LET, TokenType.CONST)) return parseVarDeclaration();
            return parseStmt();

        } catch (ParseError err) {
            tokens.synchronize();
            return null;
        }
    }

    private Stmt parseVarDeclaration() {
        boolean isConst = tokens.previous().type==TokenType.CONST;
        Token name = tokens.expect("Expected variable name", TokenType.IDENTIFIER);

        Expr initializer = null;
        if(isConst) tokens.expect("Constant variables cannot be left unassigned", TokenType.EQUAL);
        if(isConst || tokens.match(TokenType.EQUAL)) {
            initializer = parseExpression();
        }

        tokens.expect("Expected semicolon at end of declaration", TokenType.SEMICOLON);
        return new Stmt.VarDeclaration(name, initializer, isConst);
    }

    private Stmt parseStmt() {
        if(tokens.match(TokenType.PRINT)) { return parsePrintStmt(); }

        if(tokens.match(TokenType.LEFTBRACE)) {
            List<Stmt> statements = new ArrayList<>();

            while(!tokens.isAtEnd() && tokens.peek().type!=TokenType.RIGHTBRACE) {
                statements.add(declaration());
            }

            tokens.expect("SyntaxError: Expected } at end of block", TokenType.RIGHTBRACE);
            return new Stmt.Block(statements);
        }

        if(tokens.match(TokenType.RIGHTBRACE)) {
            throw error("Unexpected symbol: \"}\"", tokens.previous().line);
        }

        return parseExpressionStmt();
    }

    private Stmt parsePrintStmt() {
        Expr value = parseExpression();
        tokens.expect("Expected semicolon after expression", TokenType.SEMICOLON);
        return new Stmt.Print(value);
    }

    private Stmt parseExpressionStmt() {
        Expr expr = parseExpression();
        tokens.expect("Expected semicolon after expression", TokenType.SEMICOLON);
        return new Stmt.Expression(expr);
    }

    private Expr parseExpression() {
        int a = 1;
        return parseAssignment();
    }

    private Expr parseAssignment() {
        Expr expr = parseEquality();

        if(tokens.match(TokenType.EQUAL)) {
            Token equals = tokens.previous();
            Expr value = parseAssignment();

            if(expr instanceof Expr.Variable) {
                Token variable = ((Expr.Variable)expr).name;
                return new Expr.Assign(variable, value);
            }

            throw error("Cannot assign to expression", equals.line);
        }

        return expr;
    }

    private Expr parseEquality() {
        Expr left = parseComparison();

        while(tokens.match(TokenType.DEQUAL, TokenType.BANGEQUAL)) {
            Token operator = tokens.previous();
            Expr right = parseComparison();
            left = new Expr.BinaryExpr(left, operator, right);
        }

        return left;
    }

    private Expr parseComparison() {
        Expr left = parseTerm();

        while(tokens.match(TokenType.LESSERTHAN, TokenType.LESSERTHANEQU, TokenType.GREATERTHAN, TokenType.GREATERTHANEQU)) {
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
        if(tokens.match(TokenType.STRING)) return new Expr.Literal((String)tokens.previous().lexeme);
        if(tokens.match(TokenType.INTEGER)) return new Expr.Literal(Double.parseDouble(tokens.previous().lexeme));
        if(tokens.match(TokenType.IDENTIFIER)) return new Expr.Variable(tokens.previous());
        if(tokens.match(TokenType.TRUE)) return new Expr.Literal(true);
        if(tokens.match(TokenType.FALSE)) return new Expr.Literal(false);
        if(tokens.match(TokenType.NULL)) return new Expr.Literal(null);

        if(tokens.match(TokenType.LEFTPAR)) {
            Expr expression = parseExpression();
            tokens.expect("Expected ')' at end of expression", TokenType.RIGHTPAR);
            return new Expr.Grouping(expression);
        }
        if(tokens.match(TokenType.RIGHTPAR)) { throw error("SyntaxError: Unexpected symbol \")\"", tokens.previous().line); }

        // to-do: parse groupings
        throw error("Parse Error: Not handled token: \n\t " + tokens.peek().type, tokens.peek().line);
    }

    ParseError error(String err, int line) {
        Lox.error(err, line);
        return new ParseError();
    }
}