package jsonparser;

import constants.TokenType;

public record Token(TokenType type, Character value) {}