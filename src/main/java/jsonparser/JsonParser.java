package jsonparser;

import constants.TokenType;
import static constants.TokenType.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JsonParser {
    public Json parse(List<Token> tokens) {
        List<HashMap<String, Json>> jsonEntries = new ArrayList<>();

        List<TokenType> expectedTokenTypes = List.of(OBJECT_OPENER, OBJECT_CLOSER);
        boolean containsContent = false;

        if (tokens.isEmpty()) {
            throw new IllegalArgumentException("Error: Provided JSON file is not valid as it is empty.");
        }

        for (Token token : tokens) {
            TokenType tokenType = token.type();

            if (!expectedTokenTypes.contains(tokenType)) {
                throw new IllegalArgumentException("Error: Provided JSON file is not valid as an unexpected token '" + token.value() + "' was encountered.");
            }

            if (containsContent) {
                HashMap<String, Json> entry = new HashMap<>();
                jsonEntries.add(entry);
            }
        }

        return Json.from(jsonEntries);
    }
}
