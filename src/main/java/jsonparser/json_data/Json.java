package jsonparser.json_data;

import constants.JsonType;

public interface Json {
    JsonType getType();
    Object getValue();
}
