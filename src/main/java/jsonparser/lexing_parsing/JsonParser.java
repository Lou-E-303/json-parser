package jsonparser.lexing_parsing;

import jsonparser.exceptions.JsonSyntaxException;
import jsonparser.json_objects.*;
import jsonparser.state_management.JsonFiniteStateMachine;
import jsonparser.state_management.State;

import java.math.BigDecimal;
import java.util.List;
import java.util.Stack;

public class JsonParser {
    private final JsonFiniteStateMachine stateMachine = JsonFiniteStateMachine.JSON_FINITE_STATE_MACHINE;
    private final Stack<Json> jsonStack = new Stack<>();
    private String currentKey = null;

    public Json parse(List<Token> tokens) {

//        // TODO debug
//        for (Token token : tokens) {
//            System.out.println(token.type() + " " + token.value());
//        }
//        // TODO debug

        try {
            if (tokens.isEmpty()) {
                throw new JsonSyntaxException("Error: No tokens to process. It is possible that the provided JSON file is empty or invalid.");
            }

            for (Token token : tokens) {
                State previousState = stateMachine.getCurrentState();
                stateMachine.nextState(token.type());

                processToken(previousState, token);
            }

            if (jsonStack.size() != 1) {
                throw new IllegalStateException("Error: Invalid JSON structure. Unclosed objects or arrays remain.");
            }

            return jsonStack.pop();
        } catch (IllegalStateException e) {
            throw new JsonSyntaxException("Error: Invalid JSON syntax. " + e.getMessage());
        }
    }

    public void reset() {
        stateMachine.reset();
        currentKey = null;
        jsonStack.clear();
    }

    private void processToken(State previousState, Token token) {
        switch (token.type()) {
            case OBJECT_OPENER -> handleObjectOpener();
            case ARRAY_OPENER -> handleArrayOpener();
            case CONTENT -> handleContent(previousState, token);
            case BOOLEAN -> handleBoolean(token);
            case NUMBER -> handleNumber(token);
            case NULL -> addJsonToCurrentContext(JsonNull.getInstance());
            case OBJECT_CLOSER, ARRAY_CLOSER -> handleCloser();
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

    private void handleContent(State previousState, Token token) {
        String content = token.value().toString();

        if (previousState == State.OBJECT_KEY) {
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

            if (currentContext instanceof JsonObject) {
                if (currentKey != null) {
                    ((JsonObject) currentContext).addValue(currentKey, json);
                    currentKey = null;
                }
            } else if (currentContext instanceof JsonArray) {
                ((JsonArray) currentContext).addValue(json);
            }
        } else {
            jsonStack.push(json);
        }
    }
}
