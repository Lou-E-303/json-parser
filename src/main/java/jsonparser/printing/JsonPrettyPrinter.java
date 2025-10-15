package jsonparser.printing;

import jsonparser.json_objects.Json;

import java.util.logging.Logger;

public class JsonPrettyPrinter {
    private static final String INDENT = "  ";
    private final Logger logger = Logger.getLogger(getClass().getName());
    private int indentLevel = 0;
    private StringBuilder output = new StringBuilder();

    public void print(Json json) {
        logger.info(getFormattedJsonString(json));
    }

    public String getFormattedJsonString(Json json) {
        Object topLevelValue = json.getValue();
        output.append(topLevelValue);
        return output.toString();
    }
}
