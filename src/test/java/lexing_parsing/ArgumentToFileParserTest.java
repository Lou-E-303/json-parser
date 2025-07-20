package lexing_parsing;

import jsonparser.lexing_parsing.ArgumentToFileParser;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ArgumentToFileParserTest {
    @Test
    void givenArgsOfLengthNotOneThenThrowIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> ArgumentToFileParser.parse(new String[]{"Two", "Args"}));

        assertEquals("Error: expected exactly one argument, received 2.", exception.getMessage());
    }

    @Test
    void givenInvalidInputFileThenThrowIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> ArgumentToFileParser.parse(new String[]{"This is not a file"}));

        assertEquals("Error: input file is invalid or does not exist.", exception.getMessage());
    }

    @Test
    void givenValidInputFilepathThenReturnFile() {
        String input = "src/test/resources/pass_brackets.json";

        File testFile = new File(input);
        File parsedFile = ArgumentToFileParser.parse(new String[]{input});

        assertThat(parsedFile).isEqualTo(testFile);
    }
}
