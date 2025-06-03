package jsonparser.lexing_parsing;

import jsonparser.exceptions.JsonSyntaxException;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// Responsible for breaking the raw input into tokens and validating the token syntax

public class Lexer {
    private boolean insideString = false;
    private boolean escapeNext = false;

    public List<Token> lex(File inputFile) {
        List<Token> tokens = new ArrayList<>();
        StringBuilder stringContent = new StringBuilder();

        try (PushbackReader reader = new PushbackReader(new FileReader(inputFile))) {
            int charAsInt;
            while ((charAsInt = reader.read()) != -1) {
                char character = (char) charAsInt;

                if (insideString) {
                    if (escapeNext) {
                        switch (character) {
                            case 'n' -> stringContent.append('\n');
                            case 't' -> stringContent.append('\t');
                            case 'r' -> stringContent.append('\r');
                            case 'b' -> stringContent.append('\b');
                            case 'f' -> stringContent.append('\f');
                            case '"' -> stringContent.append('\"');
                            case '\\' -> stringContent.append('\\');
                            case '/' -> stringContent.append('/');
                            case 'u' -> {
                                char[] unicode = new char[4];
                                for (int i = 0; i < 4; i++) {
                                    int nextChar = reader.read();
                                    if (nextChar == -1) throw new JsonSyntaxException("Error: Unexpected end of input in Unicode escape.");
                                    unicode[i] = (char) nextChar;
                                }
                                stringContent.append((char) Integer.parseInt(new String(unicode), 16));
                            }
                            default -> stringContent.append(character);
                        }
                        escapeNext = false;
                    } else if (character == '\\') {
                        escapeNext = true;
                    } else if (character == '"') {
                        insideString = false;
                        tokens.add(Token.of(TokenType.CONTENT, stringContent.toString()));
                        tokens.add(Token.of(TokenType.QUOTE, '\"'));
                        stringContent.setLength(0);
                    } else {
                        stringContent.append(character);
                    }
                    continue;
                }

                // Outside string
                if (isWhitespace(character)) {
                    continue;
                }
                switch (character) {
                    case '"' -> {
                        insideString = true;
                        tokens.add(Token.of(TokenType.QUOTE, '"'));
                    }
                    case '{' -> tokens.add(Token.of(TokenType.OBJECT_OPENER, character));
                    case '}' -> tokens.add(Token.of(TokenType.OBJECT_CLOSER, character));
                    case '[' -> tokens.add(Token.of(TokenType.ARRAY_OPENER, character));
                    case ']' -> tokens.add(Token.of(TokenType.ARRAY_CLOSER, character));
                    case ':' -> tokens.add(Token.of(TokenType.COLON, character));
                    case ',' -> tokens.add(Token.of(TokenType.COMMA, character));
                    case 't' -> {
                        char[] expected = new char[3];
                        if (reader.read(expected) != 3 || !(expected[0] == 'r' && expected[1] == 'u' && expected[2] == 'e')) {
                            throw new JsonSyntaxException("Error: Invalid literal. Current sequence = " + Arrays.toString(expected));
                        }
                        tokens.add(Token.of(TokenType.BOOLEAN, true));
                    }
                    case 'f' -> {
                        char[] expected = new char[4];
                        if (reader.read(expected) != 4 || !(expected[0] == 'a' && expected[1] == 'l' && expected[2] == 's' && expected[3] == 'e')) {
                            throw new JsonSyntaxException("Error: Invalid literal. Current sequence = " + Arrays.toString(expected));
                        }
                        tokens.add(Token.of(TokenType.BOOLEAN, false));
                    }
                    case 'n' -> {
                        char[] expected = new char[3];
                        if (reader.read(expected) != 3 || !(expected[0] == 'u' && expected[1] == 'l' && expected[2] == 'l')) {
                            throw new JsonSyntaxException("Error: Invalid literal. Current sequence = " + Arrays.toString(expected));
                        }
                        tokens.add(Token.of(TokenType.NULL, null));
                    }

                    default -> {
                        // Check for valid JSON starting character
                        if (Character.isDigit(character) || character == '-') {
                            StringBuilder number = new StringBuilder();
                            number.append(character);

                            // Read the rest of the number
                            while (true) {
                                int nextInt = reader.read();
                                if (nextInt == -1) {
                                    // Handle end of file
                                    tokens.add(Token.of(TokenType.NUMBER, number.toString()));
                                    break;
                                }
                                char nextChar = (char) nextInt;

                                // Stop reading when we hit a valid delimiter
                                if (isWhitespace(nextChar) || nextChar == ',' || nextChar == '}' || nextChar == ']') {
                                    tokens.add(Token.of(TokenType.NUMBER, number.toString()));
                                    reader.unread(nextChar);
                                    break;
                                }
                                number.append(nextChar);
                            }
                        } else {
                            throw new JsonSyntaxException("Error: invalid starting character '" + character + "'");
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return tokens;
    }

    private boolean isWhitespace(char c) {
        return c == ' ' || c == '\n' || c == '\r' || c == '\t';
    }
}
