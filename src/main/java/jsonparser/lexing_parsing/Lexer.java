package jsonparser.lexing_parsing;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Lexer {
    private boolean insideString = false;
    private boolean escapeNext = false;

    public List<Token> lex(File inputFile) {
        List<Token> tokens = new ArrayList<>();
        StringBuilder stringContent = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
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
                                    if (nextChar == -1) throw new IOException("Unexpected end of input in Unicode escape.");
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
                        // TODO here we could read ahead to determine if token is true, same for false etc, similar to unicode handling above
                    }
                    default -> {
                        // Optional default case
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
