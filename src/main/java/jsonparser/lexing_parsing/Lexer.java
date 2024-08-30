package jsonparser.lexing_parsing;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Lexer {
    public List<Token> lex(File inputFile) {
        List<Token> tokens = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            int c;
            while ((c = reader.read()) > 0) {
                TokenType tokenType = getTokenType((char) c);
                Token token = new Token(tokenType, (char) c);
                tokens.add(token);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return tokens;
    }

    private TokenType getTokenType(char c) {
        return switch (c) {
            case '{' -> TokenType.OBJECT_OPENER;
            case '}' -> TokenType.OBJECT_CLOSER;
            case '[' -> TokenType.ARRAY_OPENER;
            case ']' -> TokenType.ARRAY_CLOSER;
            default -> TokenType.CONTENT;
        };
    }
}
