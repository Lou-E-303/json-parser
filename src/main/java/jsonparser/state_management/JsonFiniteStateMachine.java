package jsonparser.state_management;

import jsonparser.lexing_parsing.TokenType;

import java.util.*;

import static jsonparser.state_management.State.*;

public class JsonFiniteStateMachine {
    public static final JsonFiniteStateMachine JSON_FINITE_STATE_MACHINE = new JsonFiniteStateMachine();

    private final Deque<State> stateHistory; // Use a double-ended queue as stack for performance
    private State currentState;
    private EnumMap<State, Map<TokenType, State>> stateTransitionTable;

    private JsonFiniteStateMachine() {
        this.currentState = IDLE;
        this.stateHistory = new ArrayDeque<>();
        initialiseStateTransitionTable();
    }

    private void initialiseStateTransitionTable() {
        this.stateTransitionTable = new EnumMap<>(State.class);

        // Initialize the state transition table with empty maps for each state
        for (State state : State.values()) {
            stateTransitionTable.put(state, new EnumMap<>(TokenType.class));
        }

        stateTransitionTable.get(IDLE).put(TokenType.OBJECT_OPENER, OPEN_OBJECT);
        stateTransitionTable.get(IDLE).put(TokenType.ARRAY_OPENER, OPEN_ARRAY);

        stateTransitionTable.get(AWAITING_COLON).put(TokenType.COLON, AWAITING_VALUE);

        stateTransitionTable.get(AWAITING_VALUE).put(TokenType.CONTENT, VALUE_PARSED_IN_OBJECT);
        stateTransitionTable.get(AWAITING_VALUE).put(TokenType.OBJECT_OPENER, OPEN_OBJECT);
        stateTransitionTable.get(AWAITING_VALUE).put(TokenType.ARRAY_OPENER, OPEN_ARRAY);
        stateTransitionTable.get(AWAITING_VALUE).put(TokenType.BOOLEAN, VALUE_PARSED_IN_OBJECT);
        stateTransitionTable.get(AWAITING_VALUE).put(TokenType.NULL, VALUE_PARSED_IN_OBJECT);
        stateTransitionTable.get(AWAITING_VALUE).put(TokenType.NUMBER, VALUE_PARSED_IN_OBJECT);

        stateTransitionTable.get(OPEN_OBJECT).put(TokenType.ARRAY_OPENER, OPEN_ARRAY);
        stateTransitionTable.get(OPEN_OBJECT).put(TokenType.CONTENT, OBJECT_KEY);
        stateTransitionTable.get(OPEN_OBJECT).put(TokenType.OBJECT_CLOSER, RETURN_TO_PREVIOUS);
        stateTransitionTable.get(OPEN_OBJECT).put(TokenType.ARRAY_CLOSER, RETURN_TO_PREVIOUS);
        stateTransitionTable.get(OPEN_OBJECT).put(TokenType.COMMA, OPEN_OBJECT);

        stateTransitionTable.get(OBJECT_KEY).put(TokenType.COLON, AWAITING_VALUE);
        stateTransitionTable.get(OBJECT_KEY).put(TokenType.CONTENT, OBJECT_KEY);

        stateTransitionTable.get(OBJECT_VALUE).put(TokenType.CONTENT, VALUE_PARSED_IN_OBJECT);
        stateTransitionTable.get(OBJECT_VALUE).put(TokenType.OBJECT_CLOSER, RETURN_TO_PREVIOUS);
        stateTransitionTable.get(OBJECT_VALUE).put(TokenType.COMMA, VALUE_PARSED_IN_OBJECT);

        stateTransitionTable.get(OPEN_ARRAY).put(TokenType.OBJECT_OPENER, OPEN_OBJECT);
        stateTransitionTable.get(OPEN_ARRAY).put(TokenType.CONTENT, ARRAY_VALUE);
        stateTransitionTable.get(OPEN_ARRAY).put(TokenType.OBJECT_CLOSER, RETURN_TO_PREVIOUS);
        stateTransitionTable.get(OPEN_ARRAY).put(TokenType.ARRAY_CLOSER, RETURN_TO_PREVIOUS);
        stateTransitionTable.get(OPEN_ARRAY).put(TokenType.BOOLEAN, VALUE_PARSED_IN_ARRAY);
        stateTransitionTable.get(OPEN_ARRAY).put(TokenType.NULL, VALUE_PARSED_IN_ARRAY);
        stateTransitionTable.get(OPEN_ARRAY).put(TokenType.NUMBER, VALUE_PARSED_IN_ARRAY);
        stateTransitionTable.get(OPEN_ARRAY).put(TokenType.COMMA, OPEN_ARRAY);

        stateTransitionTable.get(ARRAY_VALUE).put(TokenType.CONTENT, VALUE_PARSED_IN_ARRAY);
        stateTransitionTable.get(ARRAY_VALUE).put(TokenType.ARRAY_CLOSER, RETURN_TO_PREVIOUS);
        stateTransitionTable.get(ARRAY_VALUE).put(TokenType.NUMBER, VALUE_PARSED_IN_ARRAY);
        stateTransitionTable.get(ARRAY_VALUE).put(TokenType.BOOLEAN, VALUE_PARSED_IN_ARRAY);
        stateTransitionTable.get(ARRAY_VALUE).put(TokenType.NULL, VALUE_PARSED_IN_ARRAY);
        stateTransitionTable.get(ARRAY_VALUE).put(TokenType.OBJECT_OPENER, OPEN_OBJECT);
        stateTransitionTable.get(ARRAY_VALUE).put(TokenType.ARRAY_OPENER, OPEN_ARRAY);
        stateTransitionTable.get(ARRAY_VALUE).put(TokenType.COMMA, VALUE_PARSED_IN_ARRAY);

        stateTransitionTable.get(VALUE_PARSED_IN_OBJECT).put(TokenType.COMMA, OPEN_OBJECT);
        stateTransitionTable.get(VALUE_PARSED_IN_OBJECT).put(TokenType.OBJECT_CLOSER, RETURN_TO_PREVIOUS);
        stateTransitionTable.get(VALUE_PARSED_IN_OBJECT).put(TokenType.ARRAY_CLOSER, RETURN_TO_PREVIOUS);

        stateTransitionTable.get(VALUE_PARSED_IN_ARRAY).put(TokenType.COMMA, OPEN_ARRAY);
        stateTransitionTable.get(VALUE_PARSED_IN_ARRAY).put(TokenType.ARRAY_CLOSER, RETURN_TO_PREVIOUS);
        stateTransitionTable.get(VALUE_PARSED_IN_ARRAY).put(TokenType.OBJECT_CLOSER, RETURN_TO_PREVIOUS);
        stateTransitionTable.get(VALUE_PARSED_IN_ARRAY).put(TokenType.NUMBER, VALUE_PARSED_IN_ARRAY);
        stateTransitionTable.get(VALUE_PARSED_IN_ARRAY).put(TokenType.BOOLEAN, VALUE_PARSED_IN_ARRAY);
        stateTransitionTable.get(VALUE_PARSED_IN_ARRAY).put(TokenType.NULL, VALUE_PARSED_IN_ARRAY);
        stateTransitionTable.get(VALUE_PARSED_IN_ARRAY).put(TokenType.CONTENT, VALUE_PARSED_IN_ARRAY);
    }

    public void nextState(TokenType currentTokenType) {
        State nextState = stateTransitionTable.get(currentState).get(currentTokenType);

        if (nextState == null) {
            throw new IllegalStateException("Cannot transition from " + currentState + " with " + currentTokenType + ".");
        }

        boolean nextStateIsAnOpenState = (nextState == OPEN_OBJECT || nextState == OPEN_ARRAY);

        if (nextStateIsAnOpenState) {
            if (currentState == AWAITING_VALUE) {
                // We need to determine the correct return state based on the context
                // Look at the state history to see if we're in an array or object context
                State contextState = determineContextState();
                stateHistory.push(contextState);
            } else if (currentState == OPEN_ARRAY) {
                stateHistory.push(VALUE_PARSED_IN_ARRAY);
            }
            // Remove the "else" case - we shouldn't push currentState for every transition to OPEN_OBJECT
        } else if (nextState == RETURN_TO_PREVIOUS) { // Special state that triggers the FSM to pop previous state from the stack.
            if (!stateHistory.isEmpty()) {
                nextState = stateHistory.pop();
            } else {
                nextState = IDLE;
            }
        }

        currentState = nextState;
    }

    private State determineContextState() {
        // Look through the state history to determine if we're in an array or object context
        for (int i = stateHistory.size() - 1; i >= 0; i--) {
            State state = new ArrayList<>(stateHistory).get(i);
            if (state == OPEN_ARRAY || state == VALUE_PARSED_IN_ARRAY) {
                return VALUE_PARSED_IN_ARRAY;
            } else if (state == OPEN_OBJECT || state == VALUE_PARSED_IN_OBJECT) {
                return VALUE_PARSED_IN_OBJECT;
            }
        }
        // Default to object context if we can't determine
        return VALUE_PARSED_IN_OBJECT;
    }

    public State getCurrentState() {
        return this.currentState;
    }

    public void reset() {
        this.currentState = IDLE;
        this.stateHistory.clear();
    }
}