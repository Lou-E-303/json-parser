import constants.Token;
import jsonparser.Lexer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LexerTest {
    private static Lexer lexer;

    @BeforeAll
    static void init() {
        lexer = new Lexer();
    }

    @Test
    void givenInputStringOfValidTokensThenReturnTokenList() {
        String input = "{}";

        List<Token> expectedTokens = List.of(Token.OPEN_BRACE, Token.CLOSED_BRACE);
        List<Token> tokens = lexer.lex(input);

        assertEquals(expectedTokens, tokens);
    }

    @Test
    void givenInputStringOfInvalidTokensThenReturnEmptyList() {
        char[] charArray = Character.toChars(0xFFFE);
        String input = new String(charArray);

        List<Token> expectedTokens = List.of();
        List<Token> tokens = lexer.lex(input);

        assertEquals(expectedTokens, tokens);
    }

    @Test
    void givenInputStringOfMixedValidAndInvalidTokensThenReturnEmptyList() {
        char[] charArray = new char[2];
        charArray[0] = '\uFFFE';
        charArray[1] = 'x';

        String input = new String(charArray);

        List<Token> expectedTokens = List.of();
        List<Token> tokens = lexer.lex(input);

        assertEquals(expectedTokens, tokens);
    }
}

