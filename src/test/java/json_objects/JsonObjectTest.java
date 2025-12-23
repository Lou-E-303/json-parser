package json_objects;

import jsonjar.error_handling.JsonReadException;
import jsonjar.json_objects.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class JsonObjectTest {
    private JsonObject jsonObject;

    @BeforeEach
    void setUp() {
        jsonObject = new JsonObject();
        jsonObject.addValue("name", new JsonString("Alice"));
        jsonObject.addValue("age", new JsonNumber(new BigDecimal("30"), "30"));
        jsonObject.addValue("active", new JsonBoolean(true));
        jsonObject.addValue("data", JsonNull.getInstance());

        JsonArray array = new JsonArray();
        array.addValue(new JsonString("Apple"));
        array.addValue(new JsonString("Banana"));
        jsonObject.addValue("items", array);

        JsonObject nested = new JsonObject();
        nested.addValue("city", new JsonString("London"));
        jsonObject.addValue("address", nested);
    }

    @Test
    void shouldPrintCorrectlyWhenToStringIsCalled() {
        jsonObject = new JsonObject();
        jsonObject.addValue("key1", new JsonString("value1"));
        jsonObject.addValue("key2", new JsonString("value2"));
        jsonObject.addValue("key3", new JsonString("value3"));
        assertEquals("{\"key1\":\"value1\",\"key2\":\"value2\",\"key3\":\"value3\"}", jsonObject.toString());
    }

    @Test
    void shouldReturnValueOnGet() {
        Json result = jsonObject.get("name");
        assertNotNull(result);
        assertInstanceOf(JsonString.class, result);
    }

    @Test
    void shouldReturnNullForMissingKey() {
        Json result = jsonObject.get("nonexistent");
        assertNull(result);
    }

    @Test
    void shouldReturnValueOnGetRequired() {
        Json result = jsonObject.getRequired("name");
        assertNotNull(result);
        assertInstanceOf(JsonString.class, result);
    }

    @Test
    void shouldThrowExceptionForMissingKey() {
        assertThrows(JsonReadException.class, () -> {
            jsonObject.getRequired("nonexistent");
        });
    }

    @Test
    void shouldThrowExceptionForWrongType() {
        assertThrows(JsonReadException.class, () -> {
            jsonObject.getAsJsonString("age");
        });
    }

    @Test
    void shouldGetJsonString() {
        JsonString result = jsonObject.getAsJsonString("name");
        assertEquals("Alice", result.getValue());
    }

    @Test
    void shouldGetString() {
        String result = jsonObject.getAsString("name");
        assertEquals("Alice", result);
    }

    @Test
    void shouldGetAsJsonNumber() {
        JsonNumber result = jsonObject.getAsJsonNumber("age");
        assertEquals(new BigDecimal("30"), result.getValue());
    }

    @Test
    void shouldGetAsBigDecimal() {
        BigDecimal result = jsonObject.getAsBigDecimal("age");
        assertEquals(new BigDecimal("30"), result);
    }

    @Test
    void shouldGetAsJsonBoolean() {
        JsonBoolean result = jsonObject.getAsJsonBoolean("active");
        assertTrue(result.getValue());
    }

    @Test
    void shouldGetAsBoolean() {
        boolean result = jsonObject.getAsBoolean("active");
        assertTrue(result);
    }

    @Test
    void shouldGetAsJsonArray() {
        JsonArray result = jsonObject.getAsJsonArray("items");
        assertEquals(2, result.getValue().size());
    }

    @Test
    void shouldGetAsArrayList() {
        ArrayList<Json> result = (ArrayList<Json>) jsonObject.getAsArrayList("items");
        assertEquals(2, result.size());
    }


    @Test
    void shouldGetJsonObject() {
        JsonObject result = jsonObject.getAsJsonObject("address");
        assertEquals("London", result.getAsString("city"));
    }

    @Test
    void shouldGetJsonNull() {
        JsonNull result = jsonObject.getAsJsonNull("data");
        assertEquals(JsonNull.getInstance(), result);
    }

    @Test
    void testNestedAccess() {
        // Test chaining getters for nested objects
        String city = jsonObject.getAsJsonObject("address").getAsString("city");
        assertEquals("London", city);
    }


}
