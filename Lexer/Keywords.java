package Lexer;

import java.util.HashMap;

public class Keywords {
    static final HashMap<String,TokenType> keywords = new HashMap<>();

    static {
        keywords.put("let", TokenType.LET);
        keywords.put("const", TokenType.CONST);
        keywords.put("true", TokenType.TRUE);
        keywords.put("false", TokenType.FALSE);
        keywords.put("null", TokenType.NULL);
    }

    public static TokenType getType(String key) {
        return keywords.get(key);
    }
}
