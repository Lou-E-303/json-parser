package jsonjar.json_objects;

public enum JsonReadingErrorConstants {
    JSON_READING_TYPE_MISMATCH("Error: The requested value for given key is not of the requested type."),
    JSON_READING_KEY_MISSING("Error: No entry for requested key ");

    private final String message;

    JsonReadingErrorConstants(String message) {
        this.message = message;
    }

    String getMessage() {
        return message;
    }
}
