package jsonparser.json_data;

import java.util.ArrayList;

public class JsonArray implements Json {
    private final ArrayList<Json> values;
    private final JsonType type = JsonType.ARRAY;

    public JsonArray() {
        this.values = new ArrayList<>();
    }

    public void addValue(Json value) {
        this.values.add(value);
    }

    @Override
    public JsonType getType() {
        return type;
    }

    @Override
    public ArrayList<Json> getValue() {
        return new ArrayList<>(values);
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
