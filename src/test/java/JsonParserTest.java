import jsonparser.json_objects.*;
import jsonparser.lexing_parsing.JsonParser;
import jsonparser.lexing_parsing.Lexer;
import jsonparser.lexing_parsing.Token;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JsonParserTest {
    private static JsonParser jsonParser;
    private static Lexer lexer;

    @BeforeEach
    void init() {
        jsonParser = new JsonParser();
        lexer = new Lexer();
    }

    @AfterEach
    void tearDown() {
        jsonParser.reset();
    }

    @Test
    void givenEmptyInputShouldReportInvalidJson() {
        List<Token> inputList = lexer.lex(new File("src/test/resources/fail_empty.json"));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> jsonParser.parse(inputList));

        assertEquals("Error: Provided JSON file is not valid as it is empty.", exception.getMessage());
    }

    @Test
    void givenRawTextInputShouldReportInvalidJson() {
        List<Token> inputList = lexer.lex(new File("src/test/resources/fail_invalid.json"));

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> jsonParser.parse(inputList));

        assertEquals("Error: Invalid JSON. Cannot transition from IDLE with CONTENT.", exception.getMessage());
    }

    @Test
    void givenObjectOpenerAndCloserInputShouldReturnSingleNullObjectEntry() {
        List<Token> inputList = lexer.lex(new File("src/test/resources/pass_brackets.json"));

        JsonObject expectedRootNode = new JsonObject();
        Json actualRootNode = jsonParser.parse(inputList);

        assertThat(actualRootNode).isEqualToComparingFieldByFieldRecursively(expectedRootNode);
    }

    @Test
    void givenArrayOpenerAndCloserInputShouldReturnSingleNullArrayEntry() {
        List<Token> inputList = lexer.lex(new File("src/test/resources/pass_simpleArray.json"));

        JsonArray expectedRootNode = new JsonArray();
        Json actualRootNode = jsonParser.parse(inputList);

        assertThat(actualRootNode).isEqualToComparingFieldByFieldRecursively(expectedRootNode);
    }

    @Test
    void givenObjectContainingContentShouldReturnValidObject() {
        List<Token> inputList = lexer.lex(new File("src/test/resources/pass_objectKeyValue.json"));

        JsonString valueString = new JsonString("value");
        JsonObject expectedRootNode = new JsonObject();
        expectedRootNode.addValue("key", valueString);

        Json actualRootNode = jsonParser.parse(inputList);

        assertThat(actualRootNode).isEqualToComparingFieldByFieldRecursively(expectedRootNode);
    }

    @Test
    void givenObjectContainingMultipleContentEntriesShouldReturnValidObject() {
        List<Token> inputList = lexer.lex(new File("src/test/resources/pass_multipleObjectKeyValues.json"));

        JsonObject expectedRootNode = new JsonObject();

        expectedRootNode.addValue("key1", new JsonString("value1"));
        expectedRootNode.addValue("key2", new JsonString("value2"));
        expectedRootNode.addValue("key3", new JsonString("value3"));

        Json actualRootNode = jsonParser.parse(inputList);

        assertThat(actualRootNode).isEqualToComparingFieldByFieldRecursively(expectedRootNode);
    }

    @Test
    void givenObjectWithEscapedQuotesThenReturnCorrectStringValues() {
        List<Token> inputList = lexer.lex(new File("src/test/resources/pass_escapedQuotes.json"));

        JsonObject expectedRootNode = new JsonObject();
        expectedRootNode.addValue("key", new JsonString("value\"with\"quotes\""));

        Json actualRootNode = jsonParser.parse(inputList);

        assertThat(actualRootNode).isEqualToComparingFieldByFieldRecursively(expectedRootNode);
    }

    @Test
    void givenObjectWithMultipleEscapeSequencesThenReturnProcessedStringValues() {
        List<Token> inputList = lexer.lex(new File("src/test/resources/pass_multipleEscapeSequences.json"));

        JsonObject expectedRootNode = new JsonObject();
        expectedRootNode.addValue("special", new JsonString("tab:\tnewline:\nquote:\"backslash:\\"));

        Json actualRootNode = jsonParser.parse(inputList);

        assertThat(actualRootNode).isEqualToComparingFieldByFieldRecursively(expectedRootNode);
    }

//    @Test
//    void givenObjectContainingBooleanValueShouldReturnValidObject() {
//        List<Token> inputList = lexer.lex(new File("src/test/resources/pass_objectBooleanValue.json"));
//
//        JsonObject expectedRootNode = new JsonObject();
//        expectedRootNode.addValue("key", new JsonBoolean(true));
//
//        Json actualRootNode = jsonParser.parse(inputList);
//
//        assertThat(actualRootNode).isEqualToComparingFieldByFieldRecursively(expectedRootNode);
//    }
}
