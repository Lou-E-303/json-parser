package jsonparser.lexing_parsing;

import jsonparser.json_data.*;
import jsonparser.state_management.JsonFiniteStateMachine;
import jsonparser.state_management.State;

import java.util.List;
import java.util.Stack;

public class JsonParser {
    private final JsonFiniteStateMachine stateMachine = JsonFiniteStateMachine.JSON_FINITE_STATE_MACHINE;
    private final Stack<Json> jsonStack = new Stack<>();
    private final StringBuilder currentString = new StringBuilder();
    private String currentKey = null;

    public Json parse(List<Token> tokens) {
        if (tokens.isEmpty()) {
            throw new IllegalArgumentException("Error: Provided JSON file is not valid as it is empty.");
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
    }

    public void reset() {
        stateMachine.reset();
        currentKey = null;
        currentString.setLength(0);
        jsonStack.clear();
    }

    private void processToken(State previousState, Token token) {
        switch (token.type()) {
            case OBJECT_OPENER -> handleObjectOpener();
            case ARRAY_OPENER -> handleArrayOpener();
            case QUOTE -> handleQuote(previousState);
            case CONTENT -> currentString.append(token.value());
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

    private void handleQuote(State previousState) {
        if (!currentString.isEmpty()) {
            String value = currentString.toString();
            currentString.setLength(0);

            if (previousState == State.OBJECT_KEY) {
                currentKey = value;
            } else {
                JsonString jsonString = JsonString.from(value);
                addJsonToCurrentContext(jsonString);
            }
        }
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
                    ((JsonObject) currentContext).setValue(currentKey, json);
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