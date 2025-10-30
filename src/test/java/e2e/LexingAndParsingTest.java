package e2e;

import jsonjar.lexing_parsing.JsonLexer;
import jsonjar.lexing_parsing.JsonParser;
import jsonjar.lexing_parsing.Token;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LexingAndParsingTest {
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
    void givenSetOfJsonInputsWhichShouldFailThenReportInvalidJson() {
        File dir = new File("src/test/resources/");
        File[] failFiles = dir.listFiles((d, name) -> name.startsWith("fail") && name.endsWith(".json"));

        assertNotNull(failFiles, "No fail files found in test resources.");

        for (File file : failFiles) {
            assertThrows(Throwable.class, () -> {
                List<Token> inputList = lexer.lexFromFile(file);
                jsonParser.parse(inputList);
            }, "Expected Exception for file: " + file.getName());
        }
    }

    @Test
    void givenSetOfJsonInputsWhichShouldPassThenReportValidJson() {
        File dir = new File("src/test/resources/");
        File[] passFiles = dir.listFiles((d, name) -> name.startsWith("pass") && name.endsWith(".json"));

        assertNotNull(passFiles, "No pass files found in test resources.");

        for (File file : passFiles) {
            assertDoesNotThrow(() -> {
                List<Token> inputList = lexer.lexFromFile(file);
                jsonParser.parse(inputList);
            });
        }
    }
}
