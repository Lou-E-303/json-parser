package jsonjar.lexing_parsing;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class LexingAndParsingTest {
    private static JsonParser jsonParser;

    @BeforeEach
    void init() {
        jsonParser = new JsonParser();
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
            assertThrows(Throwable.class, () -> jsonParser.parseFromFile(file), "Expected Exception for file: " + file.getName());
        }
    }

    @Test
    void givenSetOfJsonInputsWhichShouldPassThenReportValidJson() {
        File dir = new File("src/test/resources/");
        File[] passFiles = dir.listFiles((d, name) -> name.startsWith("pass") && name.endsWith(".json"));

        assertNotNull(passFiles, "No pass files found in test resources.");

        for (File file : passFiles) {
            assertDoesNotThrow(() -> {
                jsonParser.parseFromFile(file);
            });
        }
    }
}
