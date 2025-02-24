package jsonparser.json_data;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class JsonObject implements Json {
    private final Map<String, Json> values;
    private final JsonType type = JsonType.OBJECT;

    public JsonObject() {
        this.values = new HashMap<>();
    }

    public void addValue(String key, Json value) {
        this.values.put(key, value);
    }

    @Override
    public JsonType getType() {
        return type;
    }

    @Override
    public Map<String, Json> getValue() {
        return Collections.unmodifiableMap(values);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");

        for (Map.Entry<String, Json> entry : values.entrySet()) {
            sb.append("\"");
            sb.append(entry.getKey());
            sb.append("\":");
            sb.append(entry.getValue().toString());
            sb.append(",");
        }

        if (!values.isEmpty()) {
            sb.deleteCharAt(sb.length() - 1);
        }
        sb.append("}");
        return sb.toString();
    }
}
