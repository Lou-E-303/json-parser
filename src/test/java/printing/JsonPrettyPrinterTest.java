package printing;

import jsonparser.error_handling.JsonReadException;
import jsonparser.json_objects.Json;
import jsonparser.lexing_parsing.JsonLexer;
import jsonparser.lexing_parsing.JsonParser;
import jsonparser.printing.JsonPrettyPrinter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JsonPrettyPrinterTest {
    JsonPrettyPrinter printer;
    JsonLexer lexer;
    JsonParser parser;

    @BeforeEach
    void init() {
        printer = new JsonPrettyPrinter();
        lexer = new JsonLexer();
        parser = new JsonParser();
    }

    @Test
    void givenSimpleJsonObjectWithoutIndentationThenReturnCorrectString() throws JsonReadException {
        Json input = parser.parse(lexer.lex(new File("src/test/resources/pass_brackets.json")));
        String expected = "{}";
        assertEquals(expected, printer.getFormattedJsonString(input, 0));
    }

    @Test
    void givenSimpleJsonArrayWithoutIndentationThenReturnCorrectString() throws JsonReadException {
        Json input = parser.parse(lexer.lex(new File("src/test/resources/pass_simpleArray.json")));
        String expected = "[]";
        assertEquals(expected, printer.getFormattedJsonString(input, 0));
    }

    @Test
    void givenSimpleJsonObjectWithSingleIndentedValueThenReturnCorrectString() throws JsonReadException {
        File file = new File("src/test/resources/pass_boolean.json");
        Json input = parser.parse(lexer.lex(file));
        String expected = """
                {
                  "key": true
                }""";

        assertEquals(expected, printer.getFormattedJsonString(input, 0));
    }

    @Test
    void givenSimpleJsonArrayWithSingleIndentedValueThenReturnCorrectString() throws JsonReadException {
        File file = new File("src/test/resources/pass_singleValueArray.json");
        Json input = parser.parse(lexer.lex(file));
        String expected = """
                [
                  3
                ]""";

        assertEquals(expected, printer.getFormattedJsonString(input, 0));
    }
}
