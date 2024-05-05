package jsonparser;

import constants.Token;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Lexer {
    public List<Token> lex(File inputFile) {
        List<Token> tokens = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            int c;
            while ((c = reader.read()) > 0) {
                for (Token t : Token.values()) {
                    if (t.label == (char) c) {
                        tokens.add(t);
                        break;
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return tokens;
    }
}
