package printing;

import jsonjar.error_handling.JsonReadException;
import jsonjar.json_objects.Json;
import jsonjar.lexing_parsing.JsonLexer;
import jsonjar.lexing_parsing.JsonParser;
import jsonjar.printing.JsonPrettyPrinter;
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
        Json input = parser.parse(lexer.lexFromFile(new File("src/test/resources/pass_brackets.json")));
        String expected = "{}";
        assertEquals(expected, printer.getFormattedJsonString(input, 0));
    }

    @Test
    void givenSimpleJsonArrayWithoutIndentationThenReturnCorrectString() throws JsonReadException {
        Json input = parser.parse(lexer.lexFromFile(new File("src/test/resources/pass_simpleArray.json")));
        String expected = "[]";
        assertEquals(expected, printer.getFormattedJsonString(input, 0));
    }

    @Test
    void givenSimpleJsonObjectWithSingleIndentedValueThenReturnCorrectString() throws JsonReadException {
        File file = new File("src/test/resources/pass_boolean.json");
        Json input = parser.parse(lexer.lexFromFile(file));
        String expected = """
                {
                  "key": true
                }""";

        assertEquals(expected, printer.getFormattedJsonString(input, 0));
    }

    @Test
    void givenSimpleJsonArrayWithSingleIndentedValueThenReturnCorrectString() throws JsonReadException {
        File file = new File("src/test/resources/pass_singleValueArray.json");
        Json input = parser.parse(lexer.lexFromFile(file));
        String expected = """
                [
                  3
                ]""";

        assertEquals(expected, printer.getFormattedJsonString(input, 0));
    }

    @Test
    void givenJsonObjectWithMultipleIndentedValuesThenReturnCorrectString() throws JsonReadException {
        File file = new File("src/test/resources/pass_mixedValueObject.json");
        Json input = parser.parse(lexer.lexFromFile(file));
        String expected = """
                {
                  "key1": true,
                  "key2": false,
                  "key3": null,
                  "key4": "value",
                  "key5": 101
                }""";

        assertEquals(expected, printer.getFormattedJsonString(input, 0));
    }

    @Test
    void givenJsonArrayWithMultipleIndentedValuesThenReturnCorrectString() throws JsonReadException {
        File file = new File("src/test/resources/pass_mixedValueArray.json");
        Json input = parser.parse(lexer.lexFromFile(file));
        String expected = """
                [
                  true,
                  false,
                  null,
                  "value",
                  101
                ]""";

        assertEquals(expected, printer.getFormattedJsonString(input, 0));
    }

    @Test
    void givenMixedJsonObjectAndArrayThenReturnCorrectString() throws JsonReadException {
        File file = new File("src/test/resources/pass_mixedObjectAndArray.json");
        Json input = parser.parse(lexer.lexFromFile(file));
        String expected = """
                {
                  "key1": true,
                  "key2": [
                    false,
                    null,
                    "value",
                    101
                  ],
                  "key3": {
                    "nestedKey1": "nestedValue1",
                    "nestedKey2": 202
                  }
                }""";

        assertEquals(expected, printer.getFormattedJsonString(input, 0));
    }

    @Test
    void givenComplexArrayThenReturnCorrectlyFormattedString() throws JsonReadException {
        File file = new File("src/test/resources/pass_complexArray.json");
        Json input = parser.parse(lexer.lexFromFile(file));
        String expected = """
            [
              "JSON Test Pattern pass1",
              {
                "object with 1 member": [
                  "array with 1 element"
                ]
              },
              {},
              [],
              -42,
              true,
              false,
              null,
              {
                "integer": 1234567890,
                "real": -9876.543210,
                "e": 0.123456789e-12,
                "E": 1.234567890E+34,
                "": 23456789012E66,
                "zero": 0,
                "one": 1,
                "space": " ",
                "quote": "\\\"",
                "backslash": "\\\\",
                "controls": "\\b\\f\\n\\r\\t",
                "slash": "/ & /",
                "alpha": "abcdefghijklmnopqrstuvwyz",
                "ALPHA": "ABCDEFGHIJKLMNOPQRSTUVWYZ",
                "digit": "0123456789",
                "0123456789": "digit",
                "special": "`1~!@#$%^&*()_+-={':[,]}|;.</>?",
                "hex": "ģ䕧覫췯ꯍ",
                "true": true,
                "false": false,
                "null": null,
                "array": [],
                "object": {},
                "address": "50 St. James Street",
                "url": "http://www.JSON.org/",
                "comment": "// /* <!-- --",
                "# -- --> */": " ",
                " s p a c e d ": [
                  1,
                  2,
                  3,
                  4,
                  5,
                  6,
                  7
                ],
                "compact": [
                  1,
                  2,
                  3,
                  4,
                  5,
                  6,
                  7
                ],
                "jsontext": "{\\"object with 1 member\\":[\\"array with 1 element\\"]}",
                "quotes": "&#34; \\" %22 0x22 034 &#x22;",
                "/\\\\\\\"쫾몾ꮘﳞ볚\\b\\f\\n\\r\\t`1~!@#$%^&*()_+-=[]{}|;:',./<>?": "A key can be any string"
              },
              0.5,
              98.6,
              99.44,
              1066,
              1e1,
              0.1e1,
              1e-1,
              1e00,
              2e+00,
              2e-00,
              "rosebud"
            ]""";

        assertEquals(expected, printer.getFormattedJsonString(input, 0));
    }
}
