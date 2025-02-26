package jsonparser.json_objects;

import java.util.ArrayList;

public class JsonArray implements Json {
    private final ArrayList<Json> values;

    public JsonArray() {
        this.values = new ArrayList<>();
    }

    public void addValue(Json value) {
        this.values.add(value);
    }

    @Override
    public ArrayList<Json> getValue() {
        return new ArrayList<>(values);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof JsonArray other) {
            return this.values.equals(other.values);
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");

        for (Json value : values) {
            sb.append(value.toString());
            sb.append(",");
        }

        if (!values.isEmpty()) {
            sb.deleteCharAt(sb.length() - 1);
        }

        sb.append("]");
        return sb.toString();
    }
}
