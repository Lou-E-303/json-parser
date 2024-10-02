package jsonparser.lexing_parsing;

import jsonparser.json_data.JsonRootNode;
import jsonparser.json_data.JsonObject;
import jsonparser.json_data.JsonArray;
import jsonparser.json_data.JsonString;
import jsonparser.json_data.Json;
import jsonparser.state_management.JsonFiniteStateMachine;
import jsonparser.state_management.State;

import java.util.List;
import java.util.Stack;

public class JsonParser {
    private JsonFiniteStateMachine stateMachine = JsonFiniteStateMachine.FINITE_STATE_MACHINE;
    private final Stack<Json> jsonStack = new Stack<>();
    private String currentKey = null;

    public Json parse(List<Token> tokens) {
        if (tokens.isEmpty()) {
            throw new IllegalArgumentException("Error: Provided JSON file is not valid as it is empty.");
        }

        JsonRootNode jsonRootNode = JsonRootNode.from(null);
        jsonStack.push(jsonRootNode);

        for (Token token : tokens) {
            State previousState = stateMachine.getCurrentState();
            stateMachine.nextState(token.type());
            State currentState = stateMachine.getCurrentState();

            constructJsonHierarchy(previousState, currentState, token);
        }

        return jsonRootNode;
    }

    public void flush() {
        stateMachine = null;
        currentKey = null;
        jsonStack.clear();
    }

    private void constructJsonHierarchy(State previousState, State currentState, Token token) {
        switch (currentState) {
            case OPEN_OBJECT:
                if (previousState != State.OPEN_OBJECT) {
                    JsonObject newObject = JsonObject.from();
                    addValueToCurrentContext(newObject);
                    jsonStack.push(newObject);
                }
                break;

            case OPEN_ARRAY:
                if (previousState != State.OPEN_ARRAY) {
                    JsonArray newArray = JsonArray.from();
                    addValueToCurrentContext(newArray);
                    jsonStack.push(newArray);
                }
                break;

            case OPEN_STRING:
                String stringValue = String.valueOf(token.getValue());
                if (previousState == State.OPEN_OBJECT && currentKey == null) {
                    currentKey = stringValue;
                } else {
                    JsonString jsonString = JsonString.from(stringValue);
                    addValueToCurrentContext(jsonString);
                }
                break;

            case PREVIOUS:
                if (previousState == State.OPEN_STRING) {
                    // Already handled the string
                } else if (previousState == State.OPEN_OBJECT ||
                        previousState == State.OPEN_ARRAY) {
                    if (!jsonStack.isEmpty() && jsonStack.peek() != jsonStack.firstElement()) {
                        jsonStack.pop();
                    }
                }
                break;
        }
    }

    private void addValueToCurrentContext(Json value) {
        if (!jsonStack.isEmpty()) {
            Json currentContext = jsonStack.peek();

            if (currentContext instanceof JsonObject) {
                if (currentKey != null) {
                    ((JsonObject) currentContext).setValue(currentKey, value);
                    currentKey = null;
                }
            } else if (currentContext instanceof JsonArray) {
                ((JsonArray) currentContext).addValue(value);
            } else if (currentContext instanceof JsonRootNode) {
                ((JsonRootNode) currentContext).setValue(value);
            }
        }
    }
}
