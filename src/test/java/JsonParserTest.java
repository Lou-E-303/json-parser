import jsonparser.exceptions.JsonSyntaxException;
import jsonparser.json_objects.*;
import jsonparser.lexing_parsing.JsonParser;
import jsonparser.lexing_parsing.Lexer;
import jsonparser.lexing_parsing.Token;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.math.BigDecimal;
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

        JsonSyntaxException exception = assertThrows(JsonSyntaxException.class,
                () -> jsonParser.parse(inputList));

        assertEquals("Error: No tokens to process. It is possible that the provided JSON file is empty or invalid.", exception.getMessage());
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

    @Test
    void givenObjectContainingBooleanValueShouldReturnValidObject() {
        List<Token> inputList = lexer.lex(new File("src/test/resources/pass_objectBooleanValue.json"));

        JsonObject expectedRootNode = new JsonObject();
        expectedRootNode.addValue("key", new JsonBoolean(true));

        Json actualRootNode = jsonParser.parse(inputList);

        assertThat(actualRootNode).isEqualToComparingFieldByFieldRecursively(expectedRootNode);
    }

    @Test
    void givenObjectContainingBooleanKeyShouldReturnValidObject() {
        List<Token> inputList = lexer.lex(new File("src/test/resources/pass_objectBooleanKey.json"));

        JsonObject expectedRootNode = new JsonObject();
        expectedRootNode.addValue("true", new JsonBoolean(true));

        Json actualRootNode = jsonParser.parse(inputList);

        assertThat(actualRootNode).isEqualToComparingFieldByFieldRecursively(expectedRootNode);
    }

    @Test
    void givenObjectContainingMultipleBooleanEntriesShouldReturnValidObject() {
        List<Token> inputList = lexer.lex(new File("src/test/resources/pass_objectBooleanMultipleEntries.json"));

        JsonObject expectedRootNode = new JsonObject();
        expectedRootNode.addValue("true", new JsonBoolean(true));
        expectedRootNode.addValue("false", new JsonBoolean(false));
        expectedRootNode.addValue("key", new JsonBoolean(true));

        Json actualRootNode = jsonParser.parse(inputList);

        assertThat(actualRootNode).isEqualToComparingFieldByFieldRecursively(expectedRootNode);
    }

    @Test
    void givenObjectContainingNullValueShouldReturnValidObject() {
        List<Token> inputList = lexer.lex(new File("src/test/resources/pass_objectNullValue.json"));

        JsonObject expectedRootNode = new JsonObject();
        expectedRootNode.addValue("key", JsonNull.getInstance());

        Json actualRootNode = jsonParser.parse(inputList);

        assertThat(actualRootNode).isEqualToComparingFieldByFieldRecursively(expectedRootNode);
    }

    @Test
    void givenObjectContainingNumberValueShouldReturnValidObject() {
        List<Token> inputList = lexer.lex(new File("src/test/resources/pass_objectSingleNumber.json"));

        JsonObject expectedRootNode = new JsonObject();
        expectedRootNode.addValue("key", new JsonNumber(new BigDecimal("3")));

        Json actualRootNode = jsonParser.parse(inputList);

        assertThat(actualRootNode).isEqualToComparingFieldByFieldRecursively(expectedRootNode);
    }

    @Test
    void givenObjectContainingDecimalNumberValueShouldReturnValidObject() {
        List<Token> inputList = lexer.lex(new File("src/test/resources/pass_objectDecimalNumber.json"));

        JsonObject expectedRootNode = new JsonObject();
        expectedRootNode.addValue("key", new JsonNumber(new BigDecimal("3.14")));

        Json actualRootNode = jsonParser.parse(inputList);

        assertThat(actualRootNode).isEqualToComparingFieldByFieldRecursively(expectedRootNode);
    }

    @Test
    void givenObjectContainingScientificNotationNumberValueShouldReturnValidObject() {
        List<Token> inputList = lexer.lex(new File("src/test/resources/pass_objectScientificNotation.json"));

        JsonObject expectedRootNode = new JsonObject();
        expectedRootNode.addValue("key", new JsonNumber(new BigDecimal("123e4")));

        Json actualRootNode = jsonParser.parse(inputList);

        assertThat(actualRootNode).isEqualToComparingFieldByFieldRecursively(expectedRootNode);
    }

    @Test
    void givenObjectContainingMixedValuesShouldReturnValidObject() {
        List<Token> inputList = lexer.lex(new File("src/test/resources/pass_mixedValueInput.json"));

        JsonObject expectedRootNode = new JsonObject();
        expectedRootNode.addValue("key1", new JsonBoolean(true));
        expectedRootNode.addValue("key2", new JsonBoolean(false));
        expectedRootNode.addValue("key3", JsonNull.getInstance());
        expectedRootNode.addValue("key4", new JsonString("value"));
        expectedRootNode.addValue("key5", new JsonNumber(new BigDecimal(101)));

        Json actualRootNode = jsonParser.parse(inputList);

        assertThat(actualRootNode).isEqualToComparingFieldByFieldRecursively(expectedRootNode);
    }

    @Test
    void givenObjectContainingMixedKeysAndValuesShouldReturnValidObject() {
        List<Token> inputList = lexer.lex(new File("src/test/resources/pass_mixedKeyAndValueInput.json"));

        JsonObject expectedRootNode = new JsonObject();
        expectedRootNode.addValue("key", new JsonString("value"));
        expectedRootNode.addValue("key-n", new JsonNumber(new BigDecimal(101)));
        expectedRootNode.addValue("key-o", new JsonObject());
        expectedRootNode.addValue("key-l", new JsonArray());

        Json actualRootNode = jsonParser.parse(inputList);

        assertThat(actualRootNode).isEqualToComparingFieldByFieldRecursively(expectedRootNode);
    }

    @Test
    void givenObjectContainingNestedObjectsShouldReturnValidObject() {
        List<Token> inputList = lexer.lex(new File("src/test/resources/pass_nestedObjects.json"));

        JsonObject expectedRootNode = new JsonObject();
        JsonObject nestedObject = new JsonObject();
        nestedObject.addValue("nestedKey", new JsonString("nestedValue"));
        expectedRootNode.addValue("key", nestedObject);

        Json actualRootNode = jsonParser.parse(inputList);

        assertThat(actualRootNode).isEqualToComparingFieldByFieldRecursively(expectedRootNode);
    }

    @Test
    void givenObjectContainingManyNestedObjectsShouldReturnValidObject() {
        List<Token> inputList = lexer.lex(new File("src/test/resources/pass_manyNestedObjects.json"));

        JsonObject expectedRootNode = new JsonObject();
        JsonObject middle = new JsonObject();
        JsonObject inner = new JsonObject();
        JsonObject outer = new JsonObject();

        inner.addValue("key", new JsonString("value"));
        middle.addValue("inner", inner);
        outer.addValue("middle", middle);
        expectedRootNode.addValue("outer", outer);

        Json actualRootNode = jsonParser.parse(inputList);

        assertThat(actualRootNode).isEqualToComparingFieldByFieldRecursively(expectedRootNode);
    }

    @Test
    void givenObjectContainingIncorrectlyNestedObjectsShouldReportInvalidJson() {
        List<Token> inputList = lexer.lex(new File("src/test/resources/fail_incorrectNesting.json"));

        JsonSyntaxException exception = assertThrows(JsonSyntaxException.class,
                () -> jsonParser.parse(inputList));

        assertEquals("Error: Invalid JSON syntax. Cannot transition from OPEN_OBJECT with OBJECT_OPENER.", exception.getMessage());
    }
}
