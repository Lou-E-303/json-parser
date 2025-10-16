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

    @Test
    void givenJsonObjectWithMultipleIndentedValuesThenReturnCorrectString() throws JsonReadException {
        File file = new File("src/test/resources/pass_mixedValueObject.json");
        Json input = parser.parse(lexer.lex(file));
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
        Json input = parser.parse(lexer.lex(file));
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
        Json input = parser.parse(lexer.lex(file));
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

//    @Test
//    void givenComplexArrayThenReturnCorrectlyFormattedString() throws JsonReadException {
//        File file = new File("src/test/resources/pass_complexArray.json");
//        Json input = parser.parse(lexer.lex(file));
//        String expected = """
//            [
//              "JSON Test Pattern pass1",
//              {
//                "object with 1 member": [
//                  "array with 1 element"
//                ]
//              },
//              {},
//              [],
//              -42,
//              true,
//              false,
//              null,
//              {
//                "integer": 1234567890,
//                "real": -9876.543210,
//                "e": 1.23456789E-13,
//                "E": 1.234567890E+34,
//                "": 2.3456789012E+76,
//                "zero": 0,
//                "one": 1,
//                "space": " ",
//                "quote": "\\"",
//                "backslash": "\\\\",
//                "controls": "\\b\\f\\n\\r\\t",
//                "slash": "/ & /",
//                "alpha": "abcdefghijklmnopqrstuvwyz",
//                "ALPHA": "ABCDEFGHIJKLMNOPQRSTUVWYZ",
//                "digit": "0123456789",
//                "0123456789": "digit",
//                "special": "`1~!@#$%^&*()_+-={':[,]}|;.</>?",
//                "hex": "ģՇ趫췯ꯍ",
//                "true": true,
//                "false": false,
//                "null": null,
//                "array": [],
//                "object": {},
//                "address": "50 St. James Street",
//                "url": "http://www.JSON.org/",
//                "comment": "// /* <!-- --",
//                "# -- --> */": " ",
//                " s p a c e d ": [
//                  1,
//                  2,
//                  3,
//                  4,
//                  5,
//                  6,
//                  7
//                ],
//                "compact": [
//                  1,
//                  2,
//                  3,
//                  4,
//                  5,
//                  6,
//                  7
//                ],
//                "jsontext": "{\\"object with 1 member\\":[\\"array with 1 element\\"]}",
//                "quotes": "&#34; \\" %22 0x22 034 &#x22;",
//                "/\\\\"☃몞ﳞ캺\b\f\\n\\r\\t`1~!@#$%^&*()_+-=[]{}|;:',./<>?": "A key can be any string"
//              },
//              0.5,
//              98.6,
//              99.44,
//              1066,
//              1E+1,
//              1.0E+0,
//              1.0E-1,
//              1E+0,
//              2E+0,
//              2E+0,
//              "rosebud"
//            ]""";
//
//        assertEquals(expected, printer.getFormattedJsonString(input, 0));
//    }
}
