import jsonparser.json_data.*;
import jsonparser.lexing_parsing.JsonParser;
import jsonparser.lexing_parsing.Lexer;
import jsonparser.lexing_parsing.Token;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.util.List;
import java.util.Map;

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
    void givenObjectOpenerAndCloserInputShouldReturnSingleNullObjectEntry() {
        List<Token> inputList = lexer.lex(new File("src/test/resources/pass0_brackets.json"));

        Json expectedRootNode = JsonRootNode.from(JsonObject.from(null));
        Json actualRootNode = jsonParser.parse(inputList);

        assertThat(actualRootNode).isEqualToComparingFieldByFieldRecursively(expectedRootNode);
    }

    @Test
    void givenArrayOpenerAndCloserInputShouldReturnSingleNullArrayEntry() {
        List<Token> inputList = lexer.lex(new File("src/test/resources/pass1_array.json"));

        Json expectedRootNode = JsonRootNode.from(JsonArray.from(null));
        Json actualRootNode = jsonParser.parse(inputList);

        assertThat(actualRootNode).isEqualToComparingFieldByFieldRecursively(expectedRootNode);
    }

    @Test
    void givenObjectContainingContentShouldReturnValidObject() {
        List<Token> inputList = lexer.lex(new File("src/test/resources/pass2_content.json"));

        Json valueString = JsonString.from("value");
        Json jsonObject = JsonObject.from(Map.of("key", valueString));

        Json expectedRootNode = JsonRootNode.from(jsonObject);
        Json actualRootNode = jsonParser.parse(inputList);

        assertThat(actualRootNode).isEqualToComparingFieldByFieldRecursively(expectedRootNode);
    }
}
