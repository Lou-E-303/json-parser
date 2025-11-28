package jsonjar.lexing_parsing;

import jsonjar.error_handling.JsonSyntaxException;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static jsonjar.lexing_parsing.JsonParsingErrorConstants.*;

// Responsible for breaking the raw input into tokens and validating token syntax

class JsonLexer {
    private boolean insideString = false;
    private boolean escapeNext = false;

    List<Token> lexFromFile(File inputFile) throws IOException {
        StringBuilder fileContent = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            int charAsInt;
            while ((charAsInt = reader.read()) != -1) {
                fileContent.append((char) charAsInt);
            }
        }

        return lexFromString(fileContent.toString());
    }

    List<Token> lexFromString(String input) throws IOException {
        List<Token> tokens = new ArrayList<>();
        StringBuilder stringContent = new StringBuilder();

        try (PushbackReader reader = new PushbackReader(new StringReader(input))) {
            int charAsInt;
            while ((charAsInt = reader.read()) != -1) {
                char character = (char) charAsInt;

                if (insideString) {
                    handleString(character, stringContent, reader, tokens); // If inside a string, handle escape characters and quotes explicitly
                } else if (!isWhitespace(character)) {
                    tokeniseCharacter(character, tokens, reader); // Ignoring whitespace outside of strings, create appropriate tokens
                }
            }
        }
        reset();
        return tokens;
    }

    private void handleString(char character, StringBuilder stringContent, PushbackReader reader, List<Token> tokens) throws IOException {
        if (escapeNext) {
            handleEscapeCharacters(character, stringContent, reader); // If backslash, determine valid escape sequence and append
            escapeNext = false;
        } else if (character == '\\') {
            escapeNext = true; // Handle escape sequence on next character
        } else if (character == '"') {
            insideString = false;
            tokens.add(Token.of(TokenType.CONTENT, stringContent.toString())); // End of string, add content token and reset
            stringContent.setLength(0);
        } else {
            stringContent.append(character);
        }
    }

    private static void handleEscapeCharacters(char character, StringBuilder stringContent, PushbackReader reader) throws IOException {
        switch (character) {
            case 'n' -> stringContent.append('\n');
            case 't' -> stringContent.append('\t');
            case 'r' -> stringContent.append('\r');
            case 'b' -> stringContent.append('\b');
            case 'f' -> stringContent.append('\f');
            case '"' -> stringContent.append('\"');
            case '\\' -> stringContent.append('\\');
            case '/' -> stringContent.append('/');
            case 'u' -> handleUnicodeEscape(reader, stringContent); // Parse Unicode escape sequence as hex number and append
            default -> throw new JsonSyntaxException(LEXER_INVALID_ESCAPE_CHARACTER.getMessage() + character + "'");
        }
    }

    private static void handleUnicodeEscape(Reader reader, StringBuilder stringContent) throws IOException, JsonSyntaxException {
        char[] unicode = new char[4];
        for (int i = 0; i < 4; i++) {
            int nextChar = reader.read();
            if (nextChar == -1) throw new JsonSyntaxException(LEXER_UNEXPECTED_END_OF_UNICODE.getMessage());
            unicode[i] = (char) nextChar;
        }
        stringContent.append((char) Integer.parseInt(new String(unicode), 16));
    }

    private void tokeniseCharacter(char character, List<Token> tokens, PushbackReader reader) throws IOException {
        switch (character) {
            case '"' -> insideString = true;
            case '{' -> tokens.add(Token.of(TokenType.OBJECT_OPENER, character));
            case '}' -> tokens.add(Token.of(TokenType.OBJECT_CLOSER, character));
            case '[' -> tokens.add(Token.of(TokenType.ARRAY_OPENER, character));
            case ']' -> tokens.add(Token.of(TokenType.ARRAY_CLOSER, character));
            case ':' -> tokens.add(Token.of(TokenType.COLON, character));
            case ',' -> tokens.add(Token.of(TokenType.COMMA, character));
            case 't' -> handleBoolean(true, tokens, reader);
            case 'f' -> handleBoolean(false, tokens, reader);
            case 'n' -> handlePossibleNullLiteral(tokens, reader);

            default -> {
                // Check for valid JSON starting character
                if (Character.isDigit(character) || character == '-') {
                    tokeniseNumber(reader, character, tokens); // Read number until delimiter or EOF and then add token
                } else {
                    throw new JsonSyntaxException(LEXER_INVALID_STARTING_CHARACTER.getMessage() + character + "'");
                }
            }
        }
    }

    private static void handleBoolean(Boolean value, List<Token> tokens, PushbackReader reader) throws IOException {
        String expectedWord = Boolean.TRUE.equals(value) ? "true" : "false";

        char[] expected = new char[expectedWord.length() - 1];
        if (reader.read(expected) != expected.length) {
            throw new JsonSyntaxException(LEXER_INVALID_LITERAL.getMessage() + Arrays.toString(expected));
        }
        for (int i = 1; i < expectedWord.length(); i++) {
            if (expected[i - 1] != expectedWord.charAt(i)) {
                throw new JsonSyntaxException(LEXER_INVALID_LITERAL.getMessage() + Arrays.toString(expected));
            }
        }
        tokens.add(Token.of(TokenType.BOOLEAN, value));
    }

    private static void handlePossibleNullLiteral(List<Token> tokens, PushbackReader reader) throws IOException {
        char[] expected = new char[3];
        if (reader.read(expected) != 3 || !(expected[0] == 'u' && expected[1] == 'l' && expected[2] == 'l')) {
            throw new JsonSyntaxException(LEXER_INVALID_LITERAL.getMessage() + Arrays.toString(expected));
        }
        tokens.add(Token.of(TokenType.NULL, null));
    }

    private void tokeniseNumber(PushbackReader reader, Character character, List<Token> tokens) throws IOException {
        StringBuilder number = new StringBuilder();
        number.append(character);

        while (true) {
            int nextInt = reader.read();

            char nextChar = (char) nextInt;
            boolean endOfFile = nextInt == -1;
            boolean nextCharIsAValidDelimiter = !endOfFile && (isWhitespace(nextChar) || nextChar == ',' || nextChar == '}' || nextChar == ']');

            if (endOfFile || nextCharIsAValidDelimiter) {
                disallowLeadingZeros(number);
                tokens.add(Token.of(TokenType.NUMBER, number.toString()));
                if (nextCharIsAValidDelimiter) {
                    reader.unread(nextChar);
                }
                break;
            }

            number.append(nextChar);
        }
    }

    private static void disallowLeadingZeros(StringBuilder number) {
        if (number.charAt(0) == '0' && number.length() > 1) {
            char next = number.charAt(1);
            if (next != '.' && next != 'e' && next != 'E') {
                throw new JsonSyntaxException(LEXER_NO_LEADING_ZEROS.getMessage());
            }
        }
        if (number.length() > 2 && number.charAt(0) == '-' && number.charAt(1) == '0') {
            char next = number.charAt(2);
            if (next != '.' && next != 'e' && next != 'E') {
                throw new JsonSyntaxException(LEXER_NO_LEADING_ZEROS.getMessage());
            }
        }
    }

    private boolean isWhitespace(char c) {
        return c == ' ' || c == '\n' || c == '\r' || c == '\t';
    }

    void reset() {
        insideString = false;
        escapeNext = false;
    }
}