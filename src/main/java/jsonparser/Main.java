package jsonparser;

import jsonparser.json_objects.Json;
import jsonparser.lexing_parsing.ArgumentToFileParser;
import jsonparser.lexing_parsing.JsonParser;
import jsonparser.lexing_parsing.JsonLexer;
import jsonparser.lexing_parsing.Token;

import java.io.File;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            JsonParser parser = new JsonParser();
            File inputFile = ArgumentToFileParser.parse(args);
            List<Token> tokens = new JsonLexer().lex(inputFile);
            Json json = parser.parse(tokens);
            parser.reset();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }

        System.exit(0);
    }
}
