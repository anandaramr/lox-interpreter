package Lexer;

import java.util.HashMap;

public class Keywords {
    static final HashMap<String,TokenType> keywords = new HashMap<>();

    static {
        keywords.put("let", TokenType.LET);
        keywords.put("const", TokenType.CONST);
        keywords.put("true", TokenType.CONST);
        keywords.put("false", TokenType.CONST);
        keywords.put("null", TokenType.CONST);
    }

    public static TokenType getType(String key) {
        return keywords.get(key);
    }
}
