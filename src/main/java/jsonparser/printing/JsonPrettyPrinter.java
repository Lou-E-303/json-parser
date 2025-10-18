package jsonparser.printing;

import jsonparser.json_objects.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

public class JsonPrettyPrinter {
    private static final String INDENT = "  ";
    private final Logger logger = Logger.getLogger(getClass().getName());
    private final StringBuilder output = new StringBuilder();

    public void print(Json json) {
        logger.info(getFormattedJsonString(json, 0));
    }

    public String getFormattedJsonString(Json json, int currentIndentLevel) {
        output.setLength(0); // Clear previous output
        formatJson(json, currentIndentLevel);

        return output.toString();
    }

    private void formatJson(Json json, int currentIndentLevel) {
        switch (json) {
            case JsonObject object : handleJsonObject(object, currentIndentLevel); break;
            case JsonArray array   : handleJsonArray(array, currentIndentLevel); break;
            case JsonString str    : handleJsonString(str); break;
            case JsonNumber number : handleJsonNumber(number); break;
            case JsonBoolean bool  : handleJsonPrimitive(bool); break;
            case JsonNull jnull    : handleJsonPrimitive(jnull); break;

            default:
                throw new IllegalStateException("Unexpected Json type " + json);
        }
    }

    private void handleJsonObject(JsonObject object, int currentIndentLevel) {
        output.append("{");

        Map<String, Json> topLevelValues = object.getValue();
        for (Map.Entry<String, Json> entry : topLevelValues.entrySet()) {
            output.append("\n");
            output.append(INDENT.repeat(currentIndentLevel + 1));
            appendEscapedString(entry.getKey());
            output.append(": ");
            formatJson(entry.getValue(), currentIndentLevel + 1);
            if (!entry.equals(topLevelValues.entrySet().toArray()[topLevelValues.size() - 1])) {
                output.append(",");
            } else {
                output.append("\n").append(INDENT.repeat(currentIndentLevel));
            }
        }
        output.append("}");
    }

    private void appendEscapedString(String value) {
        output.append("\"");
        for (char c : value.toCharArray()) {
            switch (c) {
                case '"'  -> output.append("\\\"");
                case '\\' -> output.append("\\\\");
                case '\b' -> output.append("\\b");
                case '\f' -> output.append("\\f");
                case '\n' -> output.append("\\n");
                case '\r' -> output.append("\\r");
                case '\t' -> output.append("\\t");
                default   -> output.append(c);
            }
        }
        output.append("\"");
    }

    private void handleJsonArray(JsonArray array, int currentIndentLevel) {
        ArrayList<Json> elements = array.getValue();

        if (elements.isEmpty()) {
            output.append("[]");
            return;
        }

        output.append("[");

        Iterator<Json> iterator = elements.iterator(); // Use iterator to check for last element
        while (iterator.hasNext()) {
            Json element = iterator.next();
            output.append("\n");
            output.append(INDENT.repeat(currentIndentLevel + 1));
            formatJson(element, currentIndentLevel + 1);

            if (iterator.hasNext()) {
                output.append(",");
            }
        }

        output.append("\n");
        output.append(INDENT.repeat(currentIndentLevel));
        output.append("]");
    }

    private void handleJsonString(JsonString string) {
        String value = string.getValue();
        appendEscapedString(value);
    }

    private void handleJsonNumber(JsonNumber number) {
        output.append(number);
    }

    private void handleJsonPrimitive(Json primitive) {
        output.append(primitive);
    }
}
