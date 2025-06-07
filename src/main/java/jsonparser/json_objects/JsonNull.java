package jsonparser.json_objects;

public class JsonNull implements Json {
    private static final JsonNull INSTANCE = new JsonNull();
    private final String value = "null";

    private JsonNull() {
    }

    public static JsonNull getInstance() {
        return INSTANCE;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof JsonNull;
    }

    @Override
    public String toString() {
        return value;
    }
}