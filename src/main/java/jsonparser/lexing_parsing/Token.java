package jsonparser.lexing_parsing;

public record Token(TokenType type, Character value) {
    public Character getValue() {
        return value;
    }
}