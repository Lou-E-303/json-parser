package jsonparser;

import constants.TokenType;

public record Token(TokenType type, String content) {
    public Token(TokenType type, Character value) {
        this(type, value.toString());
    }

    public Character getValue() {
        return content.length() == 1 ? content.charAt(0) : null;
    }
}