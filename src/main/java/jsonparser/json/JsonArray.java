package jsonparser.json;

import constants.JsonType;

import java.util.ArrayList;

public class JsonArray implements Json {
    private final ArrayList<Json> values;
    private final JsonType type = JsonType.ARRAY;

    private JsonArray (ArrayList<Json> values) {
        this.values = values;
    }

    public static JsonArray from(ArrayList<Json> values) {
        return new JsonArray(values);
    }

    public ArrayList<Json> getValue() {
        return values;
    }

    @Override
    public JsonType getType() {
        return type;
    }
}
