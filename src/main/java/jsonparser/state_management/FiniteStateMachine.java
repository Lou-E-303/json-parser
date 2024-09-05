package jsonparser.state_management;

import jsonparser.lexing_parsing.TokenType;
import jsonparser.lexing_parsing.Token;

import java.util.EnumMap;
import java.util.Map;
import java.util.Stack;

public class FiniteStateMachine {
    public static final FiniteStateMachine FINITE_STATE_MACHINE = new FiniteStateMachine();

    private final Stack<State> stateHistory;
    private State currentState;
    private EnumMap<State, Map<TokenType, State>> stateTransitionTable;

    private FiniteStateMachine() {
       this.currentState = State.IDLE;
       this.stateHistory = new Stack<>();
       initialiseStateTransitionTable();
    }

    private void initialiseStateTransitionTable() {
        this.stateTransitionTable = new EnumMap<>(State.class);

        for (State state : State.values()) {
            stateTransitionTable.put(state, new EnumMap<>(Token.class));
        }

        stateTransitionTable.get(State.IDLE).put(TokenType.OBJECT_OPENER, State.OPEN_OBJECT);
        stateTransitionTable.get(State.IDLE).put(TokenType.ARRAY_OPENER, State.OPEN_ARRAY);

        stateTransitionTable.get(State.OPEN_OBJECT).put(TokenType.OBJECT_OPENER, State.OPEN_OBJECT);
        stateTransitionTable.get(State.OPEN_OBJECT).put(TokenType.ARRAY_OPENER, State.OPEN_ARRAY);
        stateTransitionTable.get(State.OPEN_OBJECT).put(TokenType.QUOTE, State.OPEN_STRING);
        stateTransitionTable.get(State.OPEN_OBJECT).put(TokenType.OBJECT_CLOSER, State.PREVIOUS);

        stateTransitionTable.get(State.OPEN_ARRAY).put(TokenType.ARRAY_OPENER, State.OPEN_ARRAY);
        stateTransitionTable.get(State.OPEN_ARRAY).put(TokenType.OBJECT_OPENER, State.OPEN_OBJECT);
        stateTransitionTable.get(State.OPEN_ARRAY).put(TokenType.QUOTE, State.OPEN_STRING);
        stateTransitionTable.get(State.OPEN_ARRAY).put(TokenType.ARRAY_CLOSER, State.PREVIOUS);

        stateTransitionTable.get(State.OPEN_STRING).put(TokenType.QUOTE, State.PREVIOUS);
    }

    public void nextState(TokenType currentTokenType) {
        State nextState = stateTransitionTable.get(currentState).get(currentTokenType);

        if (nextState == null) {
            throw new IllegalStateException("Error: No valid state transition for " + currentState + " using " + currentTokenType);
        }

        boolean nextStateIsAnOpenState = (
                        nextState == State.OPEN_OBJECT ||
                        nextState == State.OPEN_ARRAY ||
                        nextState == State.OPEN_STRING);

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
