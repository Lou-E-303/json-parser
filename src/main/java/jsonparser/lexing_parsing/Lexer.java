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
                        stringContent.append(character);
                        escapeNext = false;
                    } else if (character == '\\') {
                        escapeNext = true;
                    } else if (character == '"') {
                        insideString = false;
                        tokens.add(Token.of(TokenType.CONTENT, stringContent.toString()));
                        tokens.add(Token.of(TokenType.QUOTE, '"'));
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
                    case '"':
                        insideString = true;
                        tokens.add(Token.of(TokenType.QUOTE, '"'));
                        break;
                    case '{':
                        tokens.add(Token.of(TokenType.OBJECT_OPENER, character));
                        break;
                    case '}':
                        tokens.add(Token.of(TokenType.OBJECT_CLOSER, character));
                        break;
                    case '[':
                        tokens.add(Token.of(TokenType.ARRAY_OPENER, character));
                        break;
                    case ']':
                        tokens.add(Token.of(TokenType.ARRAY_CLOSER, character));
                        break;
                    case ':':
                        tokens.add(Token.of(TokenType.COLON, character));
                        break;
                    case ',':
                        tokens.add(Token.of(TokenType.COMMA, character));
                        break;
                    default:
                        break;
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
