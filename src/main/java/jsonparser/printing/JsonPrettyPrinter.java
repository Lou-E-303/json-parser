package jsonparser.printing;

import jsonparser.json_objects.*;

import java.util.Map;
import java.util.logging.Logger;

public class JsonPrettyPrinter {
    private static final String INDENT = "  ";
    private final Logger logger = Logger.getLogger(getClass().getName());
    private StringBuilder output = new StringBuilder();

    public void print(Json json) {
        logger.info(getFormattedJsonString(json, 0));
    }

    public String getFormattedJsonString(Json json, int currentIndentLevel) {
        output.setLength(0); // Clear previous output
        formatJson(json, currentIndentLevel);

        return output.toString();
    }

    private String formatJson(Json json, int currentIndentLevel) {
        switch (json) {
            case JsonObject object : handleJsonObject(object, currentIndentLevel); break;
            case JsonString str    : handleJsonPrimitive(str); break;
            case JsonNumber number : handleJsonPrimitive(number); break;
            case JsonBoolean bool  : handleJsonPrimitive(bool); break;
            case JsonNull jnull    : handleJsonPrimitive(jnull); break;

            default:
                throw new IllegalStateException("Unexpected Json type " + json);
        }

        return output.toString();
    }

    private void handleJsonObject(JsonObject object, int currentIndentLevel) {
        output.append("{");
        Map<String, Json> topLevelValues = object.getValue();
        for (Map.Entry<String, Json> entry : topLevelValues.entrySet()) {
            output.append("\n");
            output.append(INDENT);
            output.append("\"").append(entry.getKey()).append("\": ");
            formatJson(entry.getValue(), currentIndentLevel);
        }
        output.append("\n}");
    }

    private void handleJsonPrimitive(Json primitive) {
        output.append(primitive.getValue());
    }
}
