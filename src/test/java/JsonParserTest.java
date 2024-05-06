import constants.TokenType;
import jsonparser.*;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JsonParserTest {
    JsonParser jsonParser = new JsonParser();

    @Test
    void givenEmptyInputShouldReportInvalidJson() {
        List<Token> inputList = List.of();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> jsonParser.parse(inputList));

        assertEquals("Error: Provided JSON file is not valid as it is empty.", exception.getMessage());
    }

    @Test
    void givenRawTextInputShouldReportInvalidJson() {
        Token a = new Token(TokenType.CONTENT, 'a');
        Token b = new Token(TokenType.CONTENT, 'b');

        List<Token> inputList = List.of(a, b);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> jsonParser.parse(inputList));

        assertEquals("Error: Provided JSON file is not valid as an unexpected token 'a' was encountered.", exception.getMessage());
    }

    @Test
    void givenObjectOpenerAndCloserInputShouldContainSingleObjectEntry() {
        Token openBrace = new Token(TokenType.OBJECT_OPENER, '{');
        Token closedBrace = new Token(TokenType.OBJECT_CLOSER, '}');

        List<Token> inputList = List.of(openBrace, closedBrace);

        HashMap<String, Json> expectedValues = new HashMap<>();
        expectedValues.put("Object", null);
        Json expectedJsonObject = JsonObject.from(expectedValues);

        Json expectedRootNode = JsonRootNode.from(expectedJsonObject);
        Json json = jsonParser.parse(inputList);

        assertThat(json).isEqualToComparingFieldByFieldRecursively(expectedRootNode);
    }

    @Test
    void givenArrayOpenerAndCloserInputShouldShouldContainSingleArrayEntry() {
        Token openBracket = new Token(TokenType.ARRAY_OPENER, '[');
        Token closedBracket = new Token(TokenType.ARRAY_CLOSER, ']');

        List<Token> inputList = List.of(openBracket, closedBracket);

        ArrayList<Json> expectedValues = new ArrayList<>();
        expectedValues.add(null);
        Json expectedJsonArray = JsonArray.from(expectedValues);

        Json expectedRootNode = JsonRootNode.from(expectedJsonArray);
        Json json = jsonParser.parse(inputList);

        assertThat(json).isEqualToComparingFieldByFieldRecursively(expectedRootNode);
    }
}
