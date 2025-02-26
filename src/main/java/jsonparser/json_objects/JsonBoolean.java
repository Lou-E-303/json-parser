package jsonparser.json_objects;

public class JsonBoolean implements Json {
    private final boolean value;

    public JsonBoolean(boolean value) {
        this.value = value;
    }

    public Boolean getValue() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof JsonBoolean other) {
            return this.value == other.value;
        }
        return false;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
