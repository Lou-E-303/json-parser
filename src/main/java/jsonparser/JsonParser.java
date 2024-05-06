package jsonparser;

import constants.TokenType;
import static constants.TokenType.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                throw new IllegalArgumentException("Error: Provided JSON file is not valid as an unexpected token '" + token.value() + "' was encountered.");
            }

            switch (tokenType) {
                case OBJECT_OPENER:
                    expectedTokenTypes.add(OBJECT_CLOSER);
                    expectedTokenTypes.add(CONTENT);
                    break;

                case OBJECT_CLOSER:
                {
                    expectedTokenTypes.remove(CONTENT);

                    Map<String, Json> entryMap = new HashMap<>();
                    entryMap.put("Object", null);

                    JsonObject entry = JsonObject.from(entryMap);
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

                    ArrayList<Json> entryList = new ArrayList<>();
                    entryList.add(null);

                    JsonArray entry = JsonArray.from(entryList);
                    jsonRootNode.setValue(entry);
                    break;
                }
            }
        }

        return jsonRootNode;
    }
}
