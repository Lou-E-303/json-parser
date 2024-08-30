package jsonparser.json_data;

import constants.JsonType;

import java.util.Map;

public class JsonObject implements Json {
    private final Map<String, Json> values;
    private final JsonType type = JsonType.OBJECT;

    private JsonObject (Map<String, Json> values) {
        this.values = values;
    }

    public static JsonObject from(Map<String, Json> values) {
        return new JsonObject(values);
    }

    public Map<String, Json> getValue() {
        return values;
    }

    @Override
    public JsonType getType() {
        return type;
    }
}
