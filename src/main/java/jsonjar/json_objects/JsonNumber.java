package jsonjar.json_objects;

import java.math.BigDecimal;

public class JsonNumber implements Json {
    private final BigDecimal value;
    private final String originalRepresentation;

    public JsonNumber(BigDecimal value, String originalRepresentation) {
        this.value = value;
        this.originalRepresentation = originalRepresentation;
    }

    public BigDecimal getValue() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof JsonNumber jsonNumber) {
            return this.value.compareTo(jsonNumber.value) == 0;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return originalRepresentation;
    }
}
