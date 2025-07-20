package jsonparser.json_objects;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class JsonObject implements Json {
    private final Map<String, Json> values;

    public JsonObject() {
        this.values = new LinkedHashMap<>();  // Changed from HashMap to LinkedHashMap
    }

    public void addValue(String key, Json value) {
        this.values.put(key, value);
    }

    @Override
    public Map<String, Json> getValue() {
        return Collections.unmodifiableMap(values);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof JsonObject other) {
            return this.values.equals(other.values);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return values.hashCode();
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