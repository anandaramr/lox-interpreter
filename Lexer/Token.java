package Lexer;

public class Token {
    public TokenType type;
    public String lexeme;
    public int line;

    public Token(TokenType type, String lexeme, int line) {
        this.type = type;
        this.lexeme = lexeme;
        this.line = line;
    }

    @Override
    public String toString() {
        String quotes = type==TokenType.STRING ? "\"" : "";

        return "( " + type + ", " + quotes + lexeme + quotes + line + " )";
    }
}
