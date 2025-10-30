package json_objects;

import jsonjar.json_objects.JsonArray;
import jsonjar.json_objects.JsonString;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JsonArrayTest {
    @Test
    void shouldPrintCorrectlyWhenToStringIsCalled() {
        JsonArray jsonArray = new JsonArray();
        jsonArray.addValue(new JsonString("Hello"));
        jsonArray.addValue(new JsonString("World"));
        jsonArray.addValue(new JsonString("!"));
        assertEquals("[\"Hello\",\"World\",\"!\"]", jsonArray.toString());
    }
}
