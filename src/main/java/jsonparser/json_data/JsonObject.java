package jsonparser.json_data;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class JsonObject implements Json {
    private final Map<String, Json> values;
    private final JsonType type = JsonType.OBJECT;

    private JsonObject() {
        this.values = new HashMap<>();
    }

    public static JsonObject from() {
        return new JsonObject();
    }

    public void setValue(String key, Json value) {
        this.values.put(key, value);
    }

    @Override
    public JsonType getType() {
        return type;
    }

    @Override
    public Map<String, Json> getValue() {
        return Collections.unmodifiableMap(values);
    }
}
