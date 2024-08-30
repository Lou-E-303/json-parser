package jsonparser.lexing_parsing;

import constants.TokenType;
import jsonparser.json_data.Json;
import jsonparser.json_data.JsonArray;
import jsonparser.json_data.JsonObject;
import jsonparser.json_data.JsonRootNode;

import static constants.TokenType.*;

import java.util.ArrayList;
import java.util.List;

public class JsonParser {
    public Json parse(List<Token> tokens) {
        ArrayList<TokenType> expectedTokenTypes = new ArrayList<>(List.of(OBJECT_OPENER, ARRAY_OPENER));
        JsonRootNode jsonRootNode = JsonRootNode.from(null);

        if (tokens.isEmpty()) {
            throw new IllegalArgumentException("Error: Provided JSON file is not valid as it is empty.");
        }

        for (Token token : tokens) {
            TokenType tokenType = token.type();

            if (!expectedTokenTypes.contains(tokenType)) {
                throw new IllegalArgumentException("Error: Provided JSON file is not valid as an unexpected token '" + token.getValue() + "' was encountered.");
            }

            switch (tokenType) {
                case OBJECT_OPENER:
                    expectedTokenTypes.add(OBJECT_CLOSER);
                    expectedTokenTypes.add(CONTENT);
                    break;

                case OBJECT_CLOSER:
                {
                    expectedTokenTypes.remove(CONTENT);

                    JsonObject entry = JsonObject.from(null);
                    jsonRootNode.setValue(entry);
                    break;
                }

                case ARRAY_OPENER:
                {
                    expectedTokenTypes.add(ARRAY_CLOSER);
                    expectedTokenTypes.add(CONTENT);
                    break;
                }

                case ARRAY_CLOSER:
                {
                    expectedTokenTypes.remove(CONTENT);

                    JsonArray entry = JsonArray.from(null);
                    jsonRootNode.setValue(entry);
                    break;
                }
            }
        }

        return jsonRootNode;
    }
}
