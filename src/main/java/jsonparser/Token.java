package jsonparser;

import constants.TokenType;

public class Token {
    TokenType type;
    Character value;

    public Token(TokenType type, Character value) {
        this.type = type;
        this.value = value;
    }
}
