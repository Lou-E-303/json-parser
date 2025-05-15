package jsonparser.json_objects;

public class JsonNull implements Json {
    private final String value = "null";

    public JsonNull() {
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
