import jsonparser.ArgumentToFileParser;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class ArgumentToFileParserTest {
    @Test
    void givenArgsOfLengthNotOneThenThrowsIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> ArgumentToFileParser.parse(new String[]{"Two", "Args"}));

        assertEquals("Error: expected exactly one argument.", exception.getMessage());
    }

    @Test
    void givenInvalidInputFileThenThrowsIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> ArgumentToFileParser.parse(new String[]{"This is not a file"}));

        assertEquals("Error: input file is invalid or does not exist.", exception.getMessage());
    }

    @Test
    void givenValidInputFilepathThenReturnFile() {
        String input = "src/test/resources/pass0.json";

        File testFile = new File(input);
        File parsedFile = ArgumentToFileParser.parse(new String[]{input});

        assertTrue(new ReflectionEquals(testFile).matches(parsedFile));
    }
}
