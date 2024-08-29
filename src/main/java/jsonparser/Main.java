package jsonparser;

import jsonparser.json.Json;
import jsonparser.json.JsonParser;

import java.io.File;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            File inputFile = ArgumentToFileParser.parse(args);
            List<Token> tokens = new Lexer().lex(inputFile);
            Json json = new JsonParser().parse(tokens);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }

        System.out.println("JSON is valid.");
        System.exit(0);
    }
}
