package jsonparser.json_data;

import java.util.ArrayList;

public class JsonArray implements Json {
    private final ArrayList<Json> values;
    private final JsonType type = JsonType.ARRAY;

    private JsonArray() {
        this.values = new ArrayList<>();
    }

    public static JsonArray from() {
        return new JsonArray();
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
