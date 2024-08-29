package jsonparser.json;

import constants.JsonType;

public class JsonString implements Json {
    private final String value;
    private final JsonType type = JsonType.STRING;

    private JsonString(String value) {
        this.value = value;
    }

    public static JsonString from(String value) {
        return new JsonString(value);
    }

    public String getValue() {
        return value;
    }

    @Override
    public JsonType getType() {
        return type;
    }
}
