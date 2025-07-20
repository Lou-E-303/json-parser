package json_objects;

import jsonparser.json_objects.JsonString;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JsonStringTest {
    @Test
    void shouldPrintCorrectlyWhenToStringIsCalled() {
        JsonString jsonString = new JsonString("Hello, World!");
        assertEquals("\"Hello, World!\"", jsonString.toString());
    }
}
