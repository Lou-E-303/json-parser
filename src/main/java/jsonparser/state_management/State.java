package jsonparser.state_management;

public enum State {
    IDLE,
    OPEN_OBJECT,
    OPEN_ARRAY,
    ARRAY_VALUE,
    OBJECT_KEY,
    OBJECT_VALUE,
    AWAITING_COLON,
    AWAITING_VALUE,
    RETURN_TO_PREVIOUS
}
