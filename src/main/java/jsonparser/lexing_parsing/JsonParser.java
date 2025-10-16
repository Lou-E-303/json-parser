package jsonparser.lexing_parsing;

import jsonparser.error_handling.JsonSyntaxException;
import jsonparser.json_objects.*;
import jsonparser.state_management.JsonFiniteStateMachine;
import jsonparser.state_management.State;

import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

import static jsonparser.error_handling.ErrorConstants.*;

public class JsonParser {
    private static final JsonFiniteStateMachine stateMachine = JsonFiniteStateMachine.JSON_FINITE_STATE_MACHINE;
    private static final Deque<Json> jsonStack = new ArrayDeque<>();
    private String currentKey = null;

    public Json parse(List<Token> tokens) {
        reset();

        try {
            if (tokens.isEmpty()) {
                throw new JsonSyntaxException(PARSER_NO_TOKENS.getMessage());
            }

            for (Token token : tokens) {
                stateMachine.nextState(token.type());
                State currentState = stateMachine.getCurrentState();

                processToken(currentState, token);
            }

            if (jsonStack.size() != 1) {
                throw new JsonSyntaxException(PARSER_INVALID_JSON_STRUCTURE.getMessage());
            }

            return jsonStack.pop();
        } catch (IllegalStateException e) {
            throw new JsonSyntaxException(PARSER_INVALID_JSON_SYNTAX.getMessage() + e.getMessage());
        }
    }

    private void processToken(State currentState, Token token) {
        switch (token.type()) {
            case OBJECT_OPENER -> handleObjectOpener();
            case ARRAY_OPENER -> handleArrayOpener();
            case CONTENT -> handleContent(currentState, token);
            case BOOLEAN -> handleBoolean(token);
            case NUMBER -> handleNumber(token);
            case NULL -> addJsonToCurrentContext(JsonNull.getInstance());
            case OBJECT_CLOSER, ARRAY_CLOSER -> handleCloser();
            default -> {
                // Do nothing for COLON, COMMA which don't need to be added to the JSON structure
            }
        }
    }

    private void handleObjectOpener() {
        JsonObject newObject = new JsonObject();
        addJsonToCurrentContext(newObject);
        jsonStack.push(newObject);
    }

    private void handleArrayOpener() {
        JsonArray newArray = new JsonArray();
        addJsonToCurrentContext(newArray);
        jsonStack.push(newArray);
    }

    private void handleContent(State currentState, Token token) {
        String content = token.value().toString();

        if (currentState == State.OBJECT_KEY) {
            currentKey = content;
        } else {
            JsonString jsonString = new JsonString(content);
            addJsonToCurrentContext(jsonString);
        }
    }

    private void handleBoolean(Token token) {
        JsonBoolean jsonBoolean = new JsonBoolean(Boolean.parseBoolean(token.value().toString()));
        addJsonToCurrentContext(jsonBoolean);
    }

    private void handleNumber(Token token) {
        JsonNumber jsonNumber = new JsonNumber(new BigDecimal(token.value().toString()));
        addJsonToCurrentContext(jsonNumber);
    }

    private void handleCloser() {
        if (!jsonStack.isEmpty()) {
            Json completedContext = jsonStack.pop();

            if (jsonStack.isEmpty()) {
                jsonStack.push(completedContext);
            }
        }
    }

    private void addJsonToCurrentContext(Json json) {
        if (!jsonStack.isEmpty()) {
            Json currentContext = jsonStack.peek();

            if (currentContext instanceof JsonObject object) {
                if (currentKey != null) {
                    object.addValue(currentKey, json);
                    currentKey = null;
                }
            } else if (currentContext instanceof JsonArray array) {
                array.addValue(json);
            }
        } else {
            jsonStack.push(json);
        }
    }

    public void reset() {
        stateMachine.reset();
        currentKey = null;
        jsonStack.clear();
    }
}