package jsonjar.json_objects;

import jsonjar.error_handling.JsonReadException;

import java.math.BigDecimal;
import java.util.*;

import static jsonjar.json_objects.JsonReadingErrorConstants.JSON_READING_KEY_MISSING;
import static jsonjar.json_objects.JsonReadingErrorConstants.JSON_READING_TYPE_MISMATCH;

public class JsonObject implements Json {
    private final Map<String, Json> values;

    public JsonObject() {
        this.values = new LinkedHashMap<>();
    }

    public void addValue(String key, Json value) {
        this.values.put(key, value);
    }

    public Json get(String key) {
        return this.values.get(key);
    }

    public Json getRequired(String key) {
        Json value = this.values.get(key);

        if (value == null) {
            throw new JsonReadException(JSON_READING_KEY_MISSING + key);
        }

        return value;
    }

    public JsonString getAsJsonString(String key) {
        if (getRequired(key) instanceof JsonString jsonString) {
            return jsonString;
        }

        throw new JsonReadException(JSON_READING_TYPE_MISMATCH.getMessage() + " Key: " + key + " is not a JsonString.");
    }

    public String getAsString(String key) {
        if (getRequired(key) instanceof JsonString jsonString) {
            return jsonString.getValue();
        }

        throw new JsonReadException(JSON_READING_TYPE_MISMATCH.getMessage() + " Key: " + key + " is not a String.");
    }

    public JsonNumber getAsJsonNumber(String key) {
        if (getRequired(key) instanceof JsonNumber jsonNumber) {
            return jsonNumber;
        }

        throw new JsonReadException(JSON_READING_TYPE_MISMATCH.getMessage() + " Key: " + key + " is not a JsonNumber.");
    }

    public BigDecimal getAsBigDecimal(String key) {
        if (getRequired(key) instanceof JsonNumber jsonNumber) {
            return jsonNumber.getValue();
        }

        throw new JsonReadException(JSON_READING_TYPE_MISMATCH.getMessage() + " Key: " + key + " is not a BigDecimal.");
    }

    public JsonBoolean getAsJsonBoolean(String key) {
        if (getRequired(key) instanceof JsonBoolean jsonBoolean) {
            return jsonBoolean;
        }

        throw new JsonReadException(JSON_READING_TYPE_MISMATCH.getMessage() + " Key: " + key + " is not a JsonBoolean.");
    }

    public Boolean getAsBoolean(String key) {
        if (getRequired(key) instanceof JsonBoolean jsonBoolean) {
            return jsonBoolean.getValue();
        }

        throw new JsonReadException(JSON_READING_TYPE_MISMATCH.getMessage() + " Key: " + key + " is not a Boolean.");
    }

    public JsonArray getAsJsonArray(String key) {
        if (getRequired(key) instanceof JsonArray jsonArray) {
            return jsonArray;
        }

        throw new JsonReadException(JSON_READING_TYPE_MISMATCH.getMessage() + " Key: " + key + " is not a JsonArray.");
    }

    public List<Json> getAsArrayList(String key) {
        if (getRequired(key) instanceof JsonArray jsonArray) {
            return jsonArray.getValue();
        }

        throw new JsonReadException(JSON_READING_TYPE_MISMATCH.getMessage() + " Key: " + key + " is not a JsonArray.");
    }

    public JsonObject getAsJsonObject(String key) {
        if (getRequired(key) instanceof JsonObject jsonObject) {
            return jsonObject;
        }

        throw new JsonReadException(JSON_READING_TYPE_MISMATCH.getMessage() + " Key: " + key + " is not a JsonObject.");
    }

    public JsonNull getAsJsonNull(String key) {
        if (getRequired(key) instanceof JsonNull jsonNull) {
            return jsonNull;
        }

        throw new JsonReadException(JSON_READING_TYPE_MISMATCH.getMessage() + " Key: " + key + " is not a JsonNull.");
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