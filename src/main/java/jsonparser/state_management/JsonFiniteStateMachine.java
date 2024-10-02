package jsonparser.state_management;

import jsonparser.lexing_parsing.TokenType;

import java.util.EnumMap;
import java.util.Map;
import java.util.Stack;

public class JsonFiniteStateMachine {
    public static final JsonFiniteStateMachine FINITE_STATE_MACHINE = new JsonFiniteStateMachine();

    private final Stack<State> stateHistory;
    private State currentState;
    private EnumMap<State, Map<TokenType, State>> stateTransitionTable;

    private JsonFiniteStateMachine() {
        this.currentState = State.IDLE;
        this.stateHistory = new Stack<>();
        initialiseStateTransitionTable();
    }

    private void initialiseStateTransitionTable() {
        this.stateTransitionTable = new EnumMap<>(State.class);

        for (State state : State.values()) {
            stateTransitionTable.put(state, new EnumMap<>(TokenType.class));
        }

        stateTransitionTable.get(State.IDLE).put(TokenType.OBJECT_OPENER, State.OPEN_OBJECT);
        stateTransitionTable.get(State.IDLE).put(TokenType.ARRAY_OPENER, State.OPEN_ARRAY);

        stateTransitionTable.get(State.OPEN_OBJECT).put(TokenType.OBJECT_OPENER, State.OPEN_OBJECT);
        stateTransitionTable.get(State.OPEN_OBJECT).put(TokenType.ARRAY_OPENER, State.OPEN_ARRAY);
        stateTransitionTable.get(State.OPEN_OBJECT).put(TokenType.QUOTE, State.OBJECT_KEY);
        stateTransitionTable.get(State.OPEN_OBJECT).put(TokenType.OBJECT_CLOSER, State.PREVIOUS);

        stateTransitionTable.get(State.OBJECT_KEY).put(TokenType.QUOTE, State.AWAITING_COLON);
        stateTransitionTable.get(State.OBJECT_KEY).put(TokenType.CONTENT, State.OBJECT_KEY);

        stateTransitionTable.get(State.AWAITING_COLON).put(TokenType.COLON, State.AWAITING_VALUE);

        stateTransitionTable.get(State.AWAITING_VALUE).put(TokenType.QUOTE, State.OBJECT_VALUE);
        stateTransitionTable.get(State.AWAITING_VALUE).put(TokenType.OBJECT_OPENER, State.OPEN_OBJECT);
        stateTransitionTable.get(State.AWAITING_VALUE).put(TokenType.ARRAY_OPENER, State.OPEN_ARRAY);

        stateTransitionTable.get(State.OBJECT_VALUE).put(TokenType.QUOTE, State.OPEN_OBJECT);
        stateTransitionTable.get(State.OBJECT_VALUE).put(TokenType.CONTENT, State.OBJECT_VALUE);

        stateTransitionTable.get(State.OPEN_ARRAY).put(TokenType.ARRAY_OPENER, State.OPEN_ARRAY);
        stateTransitionTable.get(State.OPEN_ARRAY).put(TokenType.OBJECT_OPENER, State.OPEN_OBJECT);
        stateTransitionTable.get(State.OPEN_ARRAY).put(TokenType.QUOTE, State.ARRAY_VALUE);
        stateTransitionTable.get(State.OPEN_ARRAY).put(TokenType.ARRAY_CLOSER, State.PREVIOUS);

        stateTransitionTable.get(State.ARRAY_VALUE).put(TokenType.QUOTE, State.OPEN_ARRAY);
        stateTransitionTable.get(State.ARRAY_VALUE).put(TokenType.CONTENT, State.ARRAY_VALUE);
    }

    public void nextState(TokenType currentTokenType) {
        State nextState = stateTransitionTable.get(currentState).get(currentTokenType);

        if (nextState == null) {
            throw new IllegalStateException("Error: Invalid JSON. Cannot go from " + currentState + " to " + currentTokenType + ".");
        }

        boolean nextStateIsAnOpenState = (
                nextState == State.OPEN_OBJECT ||
                        nextState == State.OPEN_ARRAY);

        if (nextStateIsAnOpenState) {
            stateHistory.push(currentState);
        } else if (nextState == State.PREVIOUS) {
            if (!stateHistory.isEmpty()) {
                nextState = stateHistory.pop();
            } else {
                nextState = State.IDLE;
            }
        }

        currentState = nextState;
    }

    public State getCurrentState() {
        return this.currentState;
    }
}