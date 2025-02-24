import jsonparser.json_data.JsonArray;
import jsonparser.json_data.JsonString;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonArrayTest {
    @Test
    void shouldPrintCorrectlyWhenToStringIsCalled() {
        JsonArray jsonArray = new JsonArray();
        jsonArray.addValue(new JsonString("Hello"));
        jsonArray.addValue(new JsonString("World"));
        jsonArray.addValue(new JsonString("!"));
        assertEquals("[\"Hello\",\"World\",\"!\"]", jsonArray.toString());
    }
}
