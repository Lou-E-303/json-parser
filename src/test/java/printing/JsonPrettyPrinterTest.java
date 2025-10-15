package printing;

import jsonparser.exceptions.JsonReadException;
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
    void givenSimpleJsonWithoutIndentationThenReturnCorrectString() throws JsonReadException {
        Json input = parser.parse(lexer.lex(new File("src/test/resources/pass_brackets.json")));
        String expected = "{}";
        assertEquals(expected, printer.getFormattedJsonString(input, 0));
    }
}
