package jsonparser.state_management;

import constants.State;
import constants.TokenType;
import jsonparser.lexing_parsing.Token;

import java.util.EnumMap;
import java.util.Map;

public class FiniteStateMachine {
    public static final FiniteStateMachine FINITE_STATE_MACHINE = new FiniteStateMachine();
    State currentState;
    EnumMap<State, Map<TokenType, State>> stateTransitionTable;

    private FiniteStateMachine() {
       this.currentState = State.IDLE;
       initialiseStateTransitionTable();
    }

    private void initialiseStateTransitionTable() {
        this.stateTransitionTable = new EnumMap<>(State.class);

        for (State state : State.values()) {
            stateTransitionTable.put(state, new EnumMap<>(Token.class));
        }

        stateTransitionTable.get(State.IDLE).put(TokenType.OBJECT_OPENER, State.OPEN_OBJECT);
    }

    public void nextState(TokenType parsedToken) {
        State nextState = stateTransitionTable.get(currentState).get(parsedToken);

        if (nextState != null) {
            currentState = nextState;
        } else {
            throw new IllegalStateException("No valid state transition for " + currentState + " using " + parsedToken);
        }
    }

    public State getCurrentState() {
        return this.currentState;
    }
}
