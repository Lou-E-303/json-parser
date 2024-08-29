import jsonparser.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JsonParserTest {
    private static JsonParser jsonParser;
    private static Lexer lexer;

    @BeforeAll
    static void init() {
        jsonParser = new JsonParser();
        lexer = new Lexer();
    }

    @Test
    void givenEmptyInputShouldReportInvalidJson() {
        List<Token> inputList = lexer.lex(new File("src/test/resources/fail0_empty.json"));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> jsonParser.parse(inputList));

        assertEquals("Error: Provided JSON file is not valid as it is empty.", exception.getMessage());
    }

    @Test
    void givenRawTextInputShouldReportInvalidJson() {
        List<Token> inputList = lexer.lex(new File("src/test/resources/fail1_invalid.json"));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> jsonParser.parse(inputList));

        assertEquals("Error: Provided JSON file is not valid as an unexpected token 'I' was encountered.", exception.getMessage());
    }

    @Test
    void givenObjectOpenerAndCloserInputShouldReturnSingleObjectEntry() {
        List<Token> inputList = lexer.lex(new File("src/test/resources/pass0_brackets.json"));

        HashMap<String, Json> expectedValues = new HashMap<>();
        expectedValues.put("Object", null);
        Json expectedJsonObject = JsonObject.from(expectedValues);

        Json expectedRootNode = JsonRootNode.from(expectedJsonObject);
        Json json = jsonParser.parse(inputList);

        assertThat(json).isEqualToComparingFieldByFieldRecursively(expectedRootNode);
    }

    @Test
    void givenArrayOpenerAndCloserInputShouldReturnSingleArrayEntry() {
        List<Token> inputList = lexer.lex(new File("src/test/resources/pass1_array.json"));

        ArrayList<Json> expectedValues = new ArrayList<>();
        expectedValues.add(null);
        Json expectedJsonArray = JsonArray.from(expectedValues);

        Json expectedRootNode = JsonRootNode.from(expectedJsonArray);
        Json json = jsonParser.parse(inputList);

        assertThat(json).isEqualToComparingFieldByFieldRecursively(expectedRootNode);
    }
}
