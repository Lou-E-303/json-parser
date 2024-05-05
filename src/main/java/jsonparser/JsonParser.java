package jsonparser;

import constants.TokenType;
import static constants.TokenType.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JsonParser {
    public Json parse(List<Token> tokens) {
        List<HashMap<String, Json>> jsonEntries = new ArrayList<>();

        ArrayList<TokenType> expectedTokenTypes = new ArrayList<>(List.of(OBJECT_OPENER));
        boolean containsContent = false;

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
                    expectedTokenTypes.remove(OBJECT_OPENER);
                    expectedTokenTypes.add(OBJECT_CLOSER);
                    expectedTokenTypes.add(CONTENT);
                    break;

                case OBJECT_CLOSER:
                {
                    expectedTokenTypes.remove(CONTENT);
                    expectedTokenTypes.add(OBJECT_OPENER);

                    if (containsContent) {
                        HashMap<String, Json> entry = new HashMap<>();
                        jsonEntries.add(entry);
                    }

                    break;
                }

                case CONTENT:
                    containsContent = true;
                    break;

                default:
                    // Do nothing
            }
        }

        return Json.from(jsonEntries);
    }
}
