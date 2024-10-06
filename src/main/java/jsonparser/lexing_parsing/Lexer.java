package jsonparser.lexing_parsing;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Lexer {
    private boolean insideString = false;

    public List<Token> lex(File inputFile) {
        List<Token> tokens = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            int c;
            while ((c = reader.read()) > 0) {
                char character = (char) c;
                TokenType tokenType = getTokenType(character);

                if (tokenType == TokenType.QUOTE) {
                    insideString = !insideString;
                    tokens.add(new Token(tokenType, character));
                } else if (insideString || !isWhitespace(character)) {
                    tokens.add(new Token(tokenType, character));
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

    private TokenType getTokenType(char c) {
        return switch (c) {
            case '{' -> TokenType.OBJECT_OPENER;
            case '}' -> TokenType.OBJECT_CLOSER;
            case '[' -> TokenType.ARRAY_OPENER;
            case ']' -> TokenType.ARRAY_CLOSER;
            case '"' -> TokenType.QUOTE;
            case ':' -> TokenType.COLON;
            default -> TokenType.CONTENT;
        };
    }
}