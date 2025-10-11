package jsonparser.lexing_parsing;

import jsonparser.exceptions.JsonReadException;
import jsonparser.exceptions.JsonSyntaxException;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// Responsible for breaking the raw input into tokens and validating token syntax

public class JsonLexer {
    private boolean insideString = false;
    private boolean escapeNext = false;

    public List<Token> lex(File inputFile) throws JsonReadException {
        List<Token> tokens = new ArrayList<>();
        StringBuilder stringContent = new StringBuilder();

        try (PushbackReader reader = new PushbackReader(new FileReader(inputFile))) {
            int charAsInt;
            while ((charAsInt = reader.read()) != -1) {
                char character = (char) charAsInt;

                if (insideString) {
                    handleString(character, stringContent, reader, tokens); // If inside a string, handle escape characters and quotes explicitly
                } else if (!isWhitespace(character)) {
                    tokeniseCharacter(character, tokens, reader); // Ignoring whitespace outside of strings, create appropriate tokens
                }
            }
        } catch (IOException e) {
            throw new JsonReadException("Error: Failed to read provided file. " + e.getMessage());
        }

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
            default -> stringContent.append(character);
        }
    }

    private static void handleUnicodeEscape(Reader reader, StringBuilder stringContent) throws IOException {
        char[] unicode = new char[4];
        for (int i = 0; i < 4; i++) {
            int nextChar = reader.read();
            if (nextChar == -1) throw new JsonSyntaxException("Error: Unexpected end of input in Unicode escape.");
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
                    throw new JsonSyntaxException("Error: invalid starting character '" + character + "'");
                }
            }
        }
    }

    private static void handleBoolean(Boolean value, List<Token> tokens, PushbackReader reader) throws IOException {
        String expectedWord = Boolean.TRUE.equals(value) ? "true" : "false";

        char[] expected = new char[expectedWord.length() - 1];
        if (reader.read(expected) != expected.length) {
            throw new JsonSyntaxException("Error: Invalid literal. Current sequence = " + Arrays.toString(expected));
        }
        for (int i = 1; i < expectedWord.length(); i++) {
            if (expected[i - 1] != expectedWord.charAt(i)) {
                throw new JsonSyntaxException("Error: Invalid literal. Current sequence = " + Arrays.toString(expected));
            }
        }
        tokens.add(Token.of(TokenType.BOOLEAN, value));
    }

    private static void handlePossibleNullLiteral(List<Token> tokens, PushbackReader reader) throws IOException {
        char[] expected = new char[3];
        if (reader.read(expected) != 3 || !(expected[0] == 'u' && expected[1] == 'l' && expected[2] == 'l')) {
            throw new JsonSyntaxException("Error: Invalid literal. Current sequence = " + Arrays.toString(expected));
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
                tokens.add(Token.of(TokenType.NUMBER, number.toString()));
                if (nextCharIsAValidDelimiter) {
                    reader.unread(nextChar);
                }
                break;
            }

            number.append(nextChar);
        }

        if (number.indexOf("0") == number.indexOf("-") + 1) {
            throw new JsonSyntaxException("Error: Numbers cannot have leading zeros.");
        }
    }

    private boolean isWhitespace(char c) {
        return c == ' ' || c == '\n' || c == '\r' || c == '\t';
    }
}
