package jsonparser.lexing_parsing;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Lexer {
    private boolean insideString = false;
    private boolean escapeNext = false;

    public List<Token> lex(File inputFile) {
        List<Token> tokens = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            int charAsInt;
            while ((charAsInt = reader.read()) > 0) {
                char character = (char) charAsInt;
                Token token = createToken(character);

                if (character == '"' && !escapeNext) {
                    insideString = !insideString;
                }

                if (insideString && character == '\\' && !escapeNext) {
                    escapeNext = true;
                    tokens.add(token);
                    continue;
                }

                if (insideString || !isWhitespace(character)) {
                    tokens.add(token);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return tokens;
    }

    public Token createToken(char c) {
        TokenType tokenType = getTokenType(c);
        return Token.of(tokenType, c);
    }

    private boolean isWhitespace(char c) {
        return c == ' ' || c == '\n' || c == '\r' || c == '\t';
    }

    private TokenType getTokenType(char c) {
        return switch (c) {
            case '{' -> TokenType.OBJECT_OPENER;
            case '}' -> TokenType.OBJECT_CLOSER;
            case '[' -> TokenType.ARRAY_OPENER;
            case ']' -> TokenType.ARRAY_CLOSER;
            case '"' -> TokenType.QUOTE;
            case ':' -> TokenType.COLON;
            case ',' -> TokenType.COMMA;
            default -> TokenType.CONTENT;
        };
    }
}