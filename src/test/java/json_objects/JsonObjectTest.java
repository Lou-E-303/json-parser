package json_objects;

import jsonjar.json_objects.JsonObject;
import jsonjar.json_objects.JsonString;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JsonObjectTest {
    @Test
    void shouldPrintCorrectlyWhenToStringIsCalled() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addValue("key1", new JsonString("value1"));
        jsonObject.addValue("key2", new JsonString("value2"));
        jsonObject.addValue("key3", new JsonString("value3"));
        assertEquals("{\"key1\":\"value1\",\"key2\":\"value2\",\"key3\":\"value3\"}", jsonObject.toString());
    }
}
