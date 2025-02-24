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
}
