package jsonjar.error_handling;

public enum ErrorConstants {
    LEXER_FAILED_TO_READ_FILE("Error: Failed to read provided file. "),
    LEXER_INVALID_ESCAPE_CHARACTER("Error: Invalid escape character '\\"),
    LEXER_UNEXPECTED_END_OF_UNICODE("Error: Unexpected end of input in Unicode escape."),
    LEXER_INVALID_STARTING_CHARACTER("Error: invalid starting character '"),
    LEXER_INVALID_LITERAL("Error: Invalid literal. Current sequence = "),
    LEXER_NO_LEADING_ZEROS("Error: Numbers cannot have leading zeros."),
    PARSER_NO_TOKENS("Error: No tokens to process. It is possible that the provided JSON file is empty or invalid."),
    PARSER_INVALID_JSON_STRUCTURE("Error: Invalid JSON structure. Unclosed objects or arrays remain."),
    PARSER_INVALID_JSON_SYNTAX("Error: Invalid JSON syntax. ");

    private final String message;

    ErrorConstants(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
