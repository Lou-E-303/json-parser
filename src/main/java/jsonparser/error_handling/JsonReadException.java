package jsonparser.error_handling;

import java.io.IOException;

public class JsonReadException extends IOException {
    public JsonReadException(String message) {
        super(message);
    }
}
