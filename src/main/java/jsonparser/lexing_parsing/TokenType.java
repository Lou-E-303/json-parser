package jsonparser.lexing_parsing;

public enum TokenType {
    OBJECT_OPENER,
    OBJECT_CLOSER,
    ARRAY_OPENER,
    ARRAY_CLOSER,
    COLON,
    COMMA,
    BOOLEAN,
    NUMBER,
    NULL,
    CONTENT
}