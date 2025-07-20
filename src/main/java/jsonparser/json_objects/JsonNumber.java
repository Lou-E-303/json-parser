package jsonparser.json_objects;

import java.math.BigDecimal;

public class JsonNumber implements Json {
    private final BigDecimal value;

    public JsonNumber(BigDecimal value) {
        this.value = value;
    }

    public BigDecimal getValue() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof JsonNumber) {
            return this.value.compareTo(((JsonNumber) obj).value) == 0;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
