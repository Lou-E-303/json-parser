package lexing_parsing;

import jsonparser.error_handling.JsonReadException;
import jsonparser.error_handling.JsonSyntaxException;
import jsonparser.json_objects.*;
import jsonparser.lexing_parsing.JsonParser;
import jsonparser.lexing_parsing.JsonLexer;
import jsonparser.lexing_parsing.Token;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class JsonParserTest {
    private static JsonParser jsonParser;
    private static JsonLexer lexer;

    @BeforeEach
    void init() {
        jsonParser = new JsonParser();
        lexer = new JsonLexer();
    }

    @AfterEach
    void tearDown() {
        jsonParser.reset();
    }

    @Test
    void givenEmptyInputShouldReportInvalidJson() throws JsonReadException {
        List<Token> inputList = lexer.lex(new File("src/test/resources/fail_empty.json"));

        JsonSyntaxException exception = assertThrows(JsonSyntaxException.class,
                () -> jsonParser.parse(inputList));

        assertEquals("Error: No tokens to process. It is possible that the provided JSON file is empty or invalid.", exception.getMessage());
    }

    @Test
    void givenObjectOpenerAndCloserInputShouldReturnSingleNullObjectEntry() throws JsonReadException {
        List<Token> inputList = lexer.lex(new File("src/test/resources/pass_brackets.json"));

        JsonObject expectedRootNode = new JsonObject();
        Json actualRootNode = jsonParser.parse(inputList);

        assertThat(actualRootNode).isEqualToComparingFieldByFieldRecursively(expectedRootNode);
    }

    @Test
    void givenArrayOpenerAndCloserInputShouldReturnSingleNullArrayEntry() throws JsonReadException {
        List<Token> inputList = lexer.lex(new File("src/test/resources/pass_simpleArray.json"));

        JsonArray expectedRootNode = new JsonArray();
        Json actualRootNode = jsonParser.parse(inputList);

        assertThat(actualRootNode).isEqualToComparingFieldByFieldRecursively(expectedRootNode);
    }

    @Test
    void givenObjectContainingContentShouldReturnValidObject() throws JsonReadException {
        List<Token> inputList = lexer.lex(new File("src/test/resources/pass_objectKeyValue.json"));

        JsonString valueString = new JsonString("value");
        JsonObject expectedRootNode = new JsonObject();
        expectedRootNode.addValue("key", valueString);

        Json actualRootNode = jsonParser.parse(inputList);

        assertThat(actualRootNode).isEqualToComparingFieldByFieldRecursively(expectedRootNode);
    }

    @Test
    void givenObjectContainingMultipleContentEntriesShouldReturnValidObject() throws JsonReadException {
        List<Token> inputList = lexer.lex(new File("src/test/resources/pass_multipleObjectKeyValues.json"));

        JsonObject expectedRootNode = new JsonObject();

        expectedRootNode.addValue("key1", new JsonString("value1"));
        expectedRootNode.addValue("key2", new JsonString("value2"));
        expectedRootNode.addValue("key3", new JsonString("value3"));

        Json actualRootNode = jsonParser.parse(inputList);

        assertThat(actualRootNode).isEqualToComparingFieldByFieldRecursively(expectedRootNode);
    }

    @Test
    void givenObjectWithEscapedQuotesThenReturnCorrectStringValues() throws JsonReadException {
        List<Token> inputList = lexer.lex(new File("src/test/resources/pass_escapedQuotes.json"));

        JsonObject expectedRootNode = new JsonObject();
        expectedRootNode.addValue("key", new JsonString("value\"with\"quotes\""));

        Json actualRootNode = jsonParser.parse(inputList);

        assertThat(actualRootNode).isEqualToComparingFieldByFieldRecursively(expectedRootNode);
    }

    @Test
    void givenObjectWithMultipleEscapeSequencesThenReturnProcessedStringValues() throws JsonReadException {
        List<Token> inputList = lexer.lex(new File("src/test/resources/pass_multipleEscapeSequences.json"));

        JsonObject expectedRootNode = new JsonObject();
        expectedRootNode.addValue("special", new JsonString("tab:\tnewline:\nquote:\"backslash:\\"));

        Json actualRootNode = jsonParser.parse(inputList);

        assertThat(actualRootNode).isEqualToComparingFieldByFieldRecursively(expectedRootNode);
    }

    @Test
    void givenObjectContainingBooleanValueShouldReturnValidObject() throws JsonReadException {
        List<Token> inputList = lexer.lex(new File("src/test/resources/pass_objectBooleanValue.json"));

        JsonObject expectedRootNode = new JsonObject();
        expectedRootNode.addValue("key", new JsonBoolean(true));

        Json actualRootNode = jsonParser.parse(inputList);

        assertThat(actualRootNode).isEqualToComparingFieldByFieldRecursively(expectedRootNode);
    }

    @Test
    void givenObjectContainingBooleanKeyShouldReturnValidObject() throws JsonReadException {
        List<Token> inputList = lexer.lex(new File("src/test/resources/pass_objectBooleanKey.json"));

        JsonObject expectedRootNode = new JsonObject();
        expectedRootNode.addValue("true", new JsonBoolean(true));

        Json actualRootNode = jsonParser.parse(inputList);

        assertThat(actualRootNode).isEqualToComparingFieldByFieldRecursively(expectedRootNode);
    }

    @Test
    void givenObjectContainingMultipleBooleanEntriesShouldReturnValidObject() throws JsonReadException {
        List<Token> inputList = lexer.lex(new File("src/test/resources/pass_objectBooleanMultipleEntries.json"));

        JsonObject expectedRootNode = new JsonObject();
        expectedRootNode.addValue("true", new JsonBoolean(true));
        expectedRootNode.addValue("false", new JsonBoolean(false));
        expectedRootNode.addValue("key", new JsonBoolean(true));

        Json actualRootNode = jsonParser.parse(inputList);

        assertThat(actualRootNode).isEqualToComparingFieldByFieldRecursively(expectedRootNode);
    }

    @Test
    void givenObjectContainingNullValueShouldReturnValidObject() throws JsonReadException {
        List<Token> inputList = lexer.lex(new File("src/test/resources/pass_objectNullValue.json"));

        JsonObject expectedRootNode = new JsonObject();
        expectedRootNode.addValue("key", JsonNull.getInstance());

        Json actualRootNode = jsonParser.parse(inputList);

        assertThat(actualRootNode).isEqualToComparingFieldByFieldRecursively(expectedRootNode);
    }

    @Test
    void givenJsonContainingRawNumberShouldReturnValidObject() throws JsonReadException {
        List<Token> inputList = lexer.lex(new File("src/test/resources/pass_complicatedNumber.json"));

        JsonNumber expectedRootNode = new JsonNumber(new BigDecimal("-123.456e10"));

        Json actualRootNode = jsonParser.parse(inputList);

        assertThat(actualRootNode).isEqualToComparingFieldByFieldRecursively(expectedRootNode);
    }

    @Test
    void givenObjectContainingMixedValuesShouldReturnValidObject() throws JsonReadException {
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
    void givenObjectContainingMixedKeysAndValuesShouldReturnValidObject() throws JsonReadException {
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
    void givenObjectContainingNestedObjectsShouldReturnValidObject() throws JsonReadException {
        List<Token> inputList = lexer.lex(new File("src/test/resources/pass_nestedObjects.json"));

        JsonObject expectedRootNode = new JsonObject();
        JsonObject nestedObject = new JsonObject();
        nestedObject.addValue("nestedKey", new JsonString("nestedValue"));
        expectedRootNode.addValue("key", nestedObject);

        Json actualRootNode = jsonParser.parse(inputList);

        assertThat(actualRootNode).isEqualToComparingFieldByFieldRecursively(expectedRootNode);
    }

    @Test
    void givenObjectContainingManyNestedObjectsShouldReturnValidObject() throws JsonReadException {
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
    void givenObjectContainingIncorrectlyNestedObjectsShouldReportInvalidJson() throws JsonReadException {
        List<Token> inputList = lexer.lex(new File("src/test/resources/fail_incorrectNesting.json"));

        JsonSyntaxException exception = assertThrows(JsonSyntaxException.class,
                () -> jsonParser.parse(inputList));

        assertEquals("Error: Invalid JSON syntax. Cannot transition from IDLE with OBJECT_CLOSER.", exception.getMessage());
    }

    @Test
    void givenJsonObjectsNestedInArraysShouldReturnValidJson() throws JsonReadException {
        List<Token> inputList = lexer.lex(new File("src/test/resources/pass_nestedObjectsInArrays.json"));

        JsonObject deepObject = new JsonObject();
        deepObject.addValue("deepString", new JsonString("deep"));

        JsonObject arrayElement = new JsonObject();
        arrayElement.addValue("deepObject", deepObject);

        JsonArray arrayKey = new JsonArray();
        arrayKey.addValue(arrayElement);

        JsonObject topLevel = new JsonObject();
        topLevel.addValue("arrayKey", arrayKey);

        JsonObject expectedRootNode = new JsonObject();
        expectedRootNode.addValue("topLevel", topLevel);

        Json actualRootNode = jsonParser.parse(inputList);

        assertThat(actualRootNode).isEqualToComparingFieldByFieldRecursively(expectedRootNode);
    }

    @Test
    void givenCompleteJsonWithAllTypesShouldReturnValidJson() throws JsonReadException {
        List<Token> inputList = lexer.lex(new File("src/test/resources/pass_nestedComplete.json"));

        JsonObject deepObject = new JsonObject();
        deepObject.addValue("deepString", new JsonString("deep"));
        deepObject.addValue("deepNumber", new JsonNumber(new BigDecimal(99)));

        JsonObject arrayObject = new JsonObject();
        arrayObject.addValue("deepObject", deepObject);

        JsonArray arrayKey = new JsonArray();
        arrayKey.addValue(new JsonString("arrayItem"));
        arrayKey.addValue(new JsonNumber(new BigDecimal(2)));
        arrayKey.addValue(new JsonBoolean(false));
        arrayKey.addValue(JsonNull.getInstance());
        arrayKey.addValue(arrayObject);

        JsonObject objectKey = new JsonObject();
        objectKey.addValue("nestedString", new JsonString("nested"));
        JsonArray nestedArray = new JsonArray();
        nestedArray.addValue(new JsonNumber(new BigDecimal(1)));
        nestedArray.addValue(new JsonNumber(new BigDecimal(2)));
        nestedArray.addValue(new JsonNumber(new BigDecimal("3e4")));
        nestedArray.addValue(new JsonString("five"));
        objectKey.addValue("nestedArray", nestedArray);

        JsonObject topLevel = new JsonObject();
        topLevel.addValue("stringKey", new JsonString("stringValue"));
        topLevel.addValue("numberKey", new JsonNumber(new BigDecimal("123.45")));
        topLevel.addValue("booleanKey", new JsonBoolean(true));
        topLevel.addValue("nullKey", JsonNull.getInstance());
        topLevel.addValue("arrayKey", arrayKey);
        topLevel.addValue("objectKey", objectKey);

        JsonObject expectedRootNode = new JsonObject();
        expectedRootNode.addValue("topLevel", topLevel);

        Json actualRootNode = jsonParser.parse(inputList);

        assertThat(actualRootNode).isEqualToComparingFieldByFieldRecursively(expectedRootNode);
    }

    @ParameterizedTest
    @MethodSource("stringAsNumberInputs")
    void givenIncreasinglyComplexNumbersShouldReportValidJson(String inputFilePath, BigDecimal expectedNumber) throws JsonReadException {
        List<Token> inputList = lexer.lex(new File(inputFilePath));

        JsonObject expectedRootNode = new JsonObject();
        expectedRootNode.addValue("key", new JsonNumber(expectedNumber));

        Json actualRootNode = jsonParser.parse(inputList);

        assertThat(actualRootNode).isEqualToComparingFieldByFieldRecursively(expectedRootNode);
    }

    static Stream<Arguments> stringAsNumberInputs() {
        return Stream.of(
                Arguments.of("src/test/resources/pass_objectSingleNumber.json", new BigDecimal("3")),
                Arguments.of("src/test/resources/pass_objectDecimalNumber.json", new BigDecimal("3.14")),
                Arguments.of("src/test/resources/pass_objectScientificNotation.json", new BigDecimal("123e4"))
        );
    }
}
