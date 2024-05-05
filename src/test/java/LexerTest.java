import constants.TokenType;
import jsonparser.Token;
import jsonparser.Lexer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;

public class LexerTest {
    private static Lexer lexer;

    @BeforeAll
    static void init() {
        lexer = new Lexer();
    }

    @Test
    void givenInputOfValidTokensThenReturnTokenList() {
        String inputFilePath = "src/test/resources/pass0.json";

        Token openBrace = new Token(TokenType.OBJECT_OPENER, '{');
        Token closedBrace = new Token(TokenType.OBJECT_CLOSER, '}');

        ArrayList<Token> expectedTokens = new ArrayList<>(List.of(openBrace, closedBrace));
        ArrayList<Token> tokens = new ArrayList<>(lexer.lex(new File(inputFilePath)));

        assertTrue(new ReflectionEquals(expectedTokens).matches(tokens));
    }

    @Test
    void givenEmptyInputThenReturnEmptyList() {
        String inputFilePath = "src/test/resources/fail0.json";

        ArrayList<Token> expectedTokens = new ArrayList<>(List.of());
        ArrayList<Token> tokens = new ArrayList<>(lexer.lex(new File(inputFilePath)));

        assertTrue(new ReflectionEquals(expectedTokens).matches(tokens));
    }
}

