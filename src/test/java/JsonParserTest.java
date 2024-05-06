import constants.TokenType;
import jsonparser.Json;
import jsonparser.Token;
import jsonparser.JsonParser;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;

import java.util.ArrayList;
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

        HashMap<String, Json> expectedEntry = new HashMap<>();
        expectedEntry.put("Object", null);

        ArrayList<HashMap<String, Json>> expectedList = new ArrayList<>();
        expectedList.add(expectedEntry);

        Json expectedJson = Json.from(expectedList);
        Json json = jsonParser.parse(inputList);

        assertTrue(new ReflectionEquals(expectedJson).matches(json));
    }
}
