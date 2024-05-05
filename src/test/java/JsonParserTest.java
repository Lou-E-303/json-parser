import constants.TokenType;
import jsonparser.Token;
import jsonparser.JsonParser;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
}
