package jsonparser.json_data;

public class JsonString implements Json {
    private final String value;
    private final JsonType type = JsonType.STRING;

    public JsonString(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public JsonType getType() {
        return type;
    }
}
