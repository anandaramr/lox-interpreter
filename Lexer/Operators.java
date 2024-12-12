package Lexer;

import java.util.HashMap;

public class Operators {
    private final static HashMap<String, TokenType> operators =  new HashMap<>();

    static {
        operators.put("+", TokenType.PLUS);
        operators.put("-", TokenType.MINUS);
        operators.put("*", TokenType.STAR);
        operators.put("/", TokenType.FORSLASH);
        operators.put("+=", TokenType.PLUSEQUAL);
        operators.put("-=", TokenType.MINUSEQUAL);
        operators.put("*=", TokenType.STAREQUAL);
        operators.put("/=", TokenType.FORSLASHEQUAL);
        operators.put("=", TokenType.EQUAL);
        operators.put("==", TokenType.DEQUAL);
        operators.put(">", TokenType.LESSERTHAN);
        operators.put("<", TokenType.GREATERTHAN);
        operators.put(">=", TokenType.GREATERTHANEQU);
        operators.put("<=", TokenType.LESSERTHANEQU);
        operators.put("!", TokenType.BANG);
        operators.put("!=", TokenType.BANGEQUAL);
    }

    public static TokenType getType(String key) {
        return operators.get(key);
    }
}