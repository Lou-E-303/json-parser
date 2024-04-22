package constants;

public enum Token {
    OPEN_BRACE('{'),
    CLOSED_BRACE('}');

    public final char label;

    Token(char label) {
        this.label = label;
    }
}
