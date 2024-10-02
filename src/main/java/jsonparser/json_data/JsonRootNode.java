package jsonparser.json_data;

public class JsonRootNode implements Json {
    private Json value;
    private final JsonType type = JsonType.ROOT_NODE;

    private JsonRootNode (Json value) {
        this.value = value;
    }

    public static JsonRootNode from(Json value) {
        return new JsonRootNode(value);
    }

    @Override
    public JsonType getType() {
        return type;
    }

    @Override
    public Json getValue() {
        return value;
    }

    public void setValue(Json value) {
        this.value = value;
    }
}
