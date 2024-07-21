package jsonparser;

import constants.TokenType;

public record Token(TokenType type, String content) {
    public Token(TokenType type, Character value) {
        this(type, value.toString());
    }
}