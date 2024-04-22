package jsonparser;

import constants.Token;

import java.util.ArrayList;
import java.util.List;

public class Lexer {
    public List<Token> lex(String input) {
        List<Token> tokens = new ArrayList<>();

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);

            for (Token t : Token.values()) {
                if (t.label == c) {
                    tokens.add(t);
                    break;
                }
            }
        }

        return tokens;
    }
}
