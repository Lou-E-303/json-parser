package jsonparser.printing;

import jsonparser.json_objects.Json;
import jsonparser.json_objects.JsonObject;

import java.util.logging.Logger;

public class JsonPrettyPrinter {
    private static final String INDENT = "  ";
    private final Logger logger = Logger.getLogger(getClass().getName());
    private static StringBuilder output = new StringBuilder();

    public void print(Json json) {
        logger.info(getFormattedJsonString(json, 0));
    }

    public String getFormattedJsonString(Json json, int currentIndentLevel) {
        switch (json) {
            case JsonObject object : handleJsonObject(object); break;
            default:
                throw new IllegalStateException("Unexpected Json type " + json);
        }


        return output.toString();
    }

    private static void handleJsonObject(JsonObject object) {
        Object topLevelValue = object.getValue();
        output.append(topLevelValue);
    }
}
