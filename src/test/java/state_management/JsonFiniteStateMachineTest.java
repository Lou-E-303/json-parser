package state_management;

import jsonjar.lexing_parsing.TokenType;
import jsonjar.state_management.JsonFiniteStateMachine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static jsonjar.state_management.State.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JsonFiniteStateMachineTest {
    JsonFiniteStateMachine jsonFiniteStateMachine;

    @BeforeEach
    void init() {
        jsonFiniteStateMachine = JsonFiniteStateMachine.JSON_FINITE_STATE_MACHINE;
    }

    @Test
    void givenNextStateIsCalledThenTransitionToCorrectState() {
        jsonFiniteStateMachine.nextState(TokenType.OBJECT_OPENER);
        assertEquals(OPEN_OBJECT, jsonFiniteStateMachine.getCurrentState());

        jsonFiniteStateMachine.nextState(TokenType.ARRAY_OPENER);
        assertEquals(OPEN_ARRAY, jsonFiniteStateMachine.getCurrentState());
    }

    @Test
    void givenInvalidStateTransitionThenThrowException() {
        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> jsonFiniteStateMachine.nextState(TokenType.CONTENT));

        assertEquals("Cannot transition from IDLE with CONTENT.", exception.getMessage());
    }
}
