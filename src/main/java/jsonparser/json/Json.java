package jsonparser.json;

import constants.JsonType;

public interface Json {
    JsonType getType();
    Object getValue();
}
