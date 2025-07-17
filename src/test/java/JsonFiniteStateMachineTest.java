import jsonparser.lexing_parsing.TokenType;
import jsonparser.state_management.JsonFiniteStateMachine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static jsonparser.state_management.State.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class JsonFiniteStateMachineTest {
    JsonFiniteStateMachine jsonFiniteStateMachine;

    @BeforeEach
    void init() {
        jsonFiniteStateMachine = JsonFiniteStateMachine.JSON_FINITE_STATE_MACHINE;
    }

    @Test
    public void givenNextStateIsCalledThenTransitionToCorrectState() {
        jsonFiniteStateMachine.nextState(TokenType.OBJECT_OPENER);
        assertEquals(OPEN_OBJECT, jsonFiniteStateMachine.getCurrentState());

        jsonFiniteStateMachine.nextState(TokenType.QUOTE);
        assertEquals(OBJECT_KEY, jsonFiniteStateMachine.getCurrentState());

        jsonFiniteStateMachine.nextState(TokenType.QUOTE);
        assertEquals(AWAITING_COLON, jsonFiniteStateMachine.getCurrentState());

        jsonFiniteStateMachine.nextState(TokenType.COLON);
        assertEquals(AWAITING_VALUE, jsonFiniteStateMachine.getCurrentState());
    }

    @Test
    public void givenInvalidStateTransitionThenThrowException() {
        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> jsonFiniteStateMachine.nextState(TokenType.CONTENT));

        assertEquals("Cannot transition from IDLE with CONTENT.", exception.getMessage());
    }
}
