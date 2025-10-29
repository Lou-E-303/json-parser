package jsonparser;

import jsonparser.error_handling.JsonReadException;
import jsonparser.json_objects.Json;
import jsonparser.lexing_parsing.JsonLexer;
import jsonparser.lexing_parsing.JsonParser;
import jsonparser.lexing_parsing.Token;

import java.io.File;
import java.util.List;

public class JsonJar {
    static JsonLexer lexer = new JsonLexer();
    static JsonParser parser = new JsonParser();

    public static Json parse(String jsonString) {
        List<Token> tokens = lexer.lexFromString(jsonString);
        return parser.parse(tokens);
    }

    public static Json parse(File file) throws JsonReadException {
        List<Token> tokens = lexer.lexFromFile(file);
        return parser.parse(tokens);
    }
}
