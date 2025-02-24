import jsonparser.json_data.*;
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

        JsonString valueString = JsonString.from("value");
        JsonObject expectedRootNode = new JsonObject();
        expectedRootNode.setValue("key", valueString);

        Json actualRootNode = jsonParser.parse(inputList);

        assertThat(actualRootNode).isEqualToComparingFieldByFieldRecursively(expectedRootNode);
    }

    @Test
    void givenObjectContainingMultipleContentEntriesShouldReturnValidObject() {
        List<Token> inputList = lexer.lex(new File("src/test/resources/pass_multipleObjectKeyValues.json"));

        JsonObject expectedRootNode = new JsonObject();

        expectedRootNode.setValue("key1", JsonString.from("value1"));
        expectedRootNode.setValue("key2", JsonString.from("value2"));
        expectedRootNode.setValue("key3", JsonString.from("value3"));

        Json actualRootNode = jsonParser.parse(inputList);

        assertThat(actualRootNode).isEqualToComparingFieldByFieldRecursively(expectedRootNode);
    }
}
