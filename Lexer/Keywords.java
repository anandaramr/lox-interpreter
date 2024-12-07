package Lexer;

import java.util.HashMap;

public class Keywords {
    static final HashMap<String,TokenType> keywords = new HashMap<>();

    static {
        keywords.put("say", TokenType.SAY);
        keywords.put(";", TokenType.SEMICOLON);
        keywords.put("+", TokenType.PLUS);
    }

    public static TokenType getType(String key) {
        return keywords.get(key);
    }
}
