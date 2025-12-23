package jsonjar.error_handling;

public class JsonReadException extends RuntimeException {
    public JsonReadException(String message) {
        super(message);
    }
}
