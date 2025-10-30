package jsonjar.error_handling;

public class JsonSyntaxException extends RuntimeException {
    public JsonSyntaxException(String message) {
        super(message);
    }
}
