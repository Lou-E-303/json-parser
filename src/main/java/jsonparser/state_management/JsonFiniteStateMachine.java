package jsonparser.state_management;

import jsonparser.lexing_parsing.TokenType;

import java.util.EnumMap;
import java.util.Map;
import java.util.Stack;

import static jsonparser.state_management.State.*;

public class JsonFiniteStateMachine {
    public static final JsonFiniteStateMachine JSON_FINITE_STATE_MACHINE = new JsonFiniteStateMachine();

    private final Stack<State> stateHistory;
    private State currentState;
    private EnumMap<State, Map<TokenType, State>> stateTransitionTable;

    private JsonFiniteStateMachine() {
        this.currentState = IDLE;
        this.stateHistory = new Stack<>();
        initialiseStateTransitionTable();
    }

    private void initialiseStateTransitionTable() {
        this.stateTransitionTable = new EnumMap<>(State.class);

        for (State state : values()) {
            stateTransitionTable.put(state, new EnumMap<>(TokenType.class));
        }

        stateTransitionTable.get(IDLE).put(TokenType.OBJECT_OPENER, OPEN_OBJECT);
        stateTransitionTable.get(IDLE).put(TokenType.ARRAY_OPENER, OPEN_ARRAY);

        stateTransitionTable.get(OPEN_OBJECT).put(TokenType.OBJECT_OPENER, OPEN_OBJECT);
        stateTransitionTable.get(OPEN_OBJECT).put(TokenType.ARRAY_OPENER, OPEN_ARRAY);
        stateTransitionTable.get(OPEN_OBJECT).put(TokenType.QUOTE, OBJECT_KEY);
        stateTransitionTable.get(OPEN_OBJECT).put(TokenType.OBJECT_CLOSER, RETURN_TO_PREVIOUS);
        stateTransitionTable.get(OPEN_OBJECT).put(TokenType.COMMA, OPEN_OBJECT);

        stateTransitionTable.get(OBJECT_KEY).put(TokenType.QUOTE, AWAITING_COLON);
        stateTransitionTable.get(OBJECT_KEY).put(TokenType.CONTENT, OBJECT_KEY);

        stateTransitionTable.get(AWAITING_COLON).put(TokenType.COLON, AWAITING_VALUE);

        stateTransitionTable.get(AWAITING_VALUE).put(TokenType.QUOTE, OBJECT_VALUE);
        stateTransitionTable.get(AWAITING_VALUE).put(TokenType.OBJECT_OPENER, OPEN_OBJECT);
        stateTransitionTable.get(AWAITING_VALUE).put(TokenType.ARRAY_OPENER, OPEN_ARRAY);

        stateTransitionTable.get(OBJECT_VALUE).put(TokenType.QUOTE, OPEN_OBJECT);
        stateTransitionTable.get(OBJECT_VALUE).put(TokenType.CONTENT, OBJECT_VALUE);

        stateTransitionTable.get(OPEN_ARRAY).put(TokenType.ARRAY_OPENER, OPEN_ARRAY);
        stateTransitionTable.get(OPEN_ARRAY).put(TokenType.OBJECT_OPENER, OPEN_OBJECT);
        stateTransitionTable.get(OPEN_ARRAY).put(TokenType.QUOTE, ARRAY_VALUE);
        stateTransitionTable.get(OPEN_ARRAY).put(TokenType.ARRAY_CLOSER, RETURN_TO_PREVIOUS);

        stateTransitionTable.get(ARRAY_VALUE).put(TokenType.QUOTE, OPEN_ARRAY);
        stateTransitionTable.get(ARRAY_VALUE).put(TokenType.CONTENT, ARRAY_VALUE);
    }

    public void nextState(TokenType currentTokenType) {
        State nextState = stateTransitionTable.get(currentState).get(currentTokenType);

        if (nextState == null) {
            throw new IllegalStateException("Error: Invalid JSON. Cannot transition from " + currentState + " with " + currentTokenType + ".");
        }

        boolean nextStateIsAnOpenState = (nextState == OPEN_OBJECT || nextState == OPEN_ARRAY);

        if (nextStateIsAnOpenState) {
            stateHistory.push(currentState);
        } else if (nextState == RETURN_TO_PREVIOUS) {
            if (!stateHistory.isEmpty()) {
                nextState = stateHistory.pop();
            } else {
                nextState = IDLE;
            }
        }

        currentState = nextState;
    }

    public State getCurrentState() {
        return this.currentState;
    }
}