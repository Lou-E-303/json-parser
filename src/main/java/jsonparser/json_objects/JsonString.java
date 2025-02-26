package jsonparser.json_objects;

public class JsonString implements Json {
    private final String value;

    public JsonString(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof JsonString other) {
            return this.value.equals(other.value);
        }
        return false;
    }

    @Override
    public String toString() {
        return "\"" + value + "\"";
    }
}
