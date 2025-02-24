import jsonparser.json_data.JsonString;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonStringTest {
    @Test
    void shouldPrintCorrectlyWhenToStringIsCalled() {
        JsonString jsonString = new JsonString("Hello, World!");
        assertEquals("\"Hello, World!\"", jsonString.toString());
    }
}
