import constants.Token;
import jsonparser.Lexer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LexerTest {
    private static Lexer lexer;

    @BeforeAll
    static void init() {
        lexer = new Lexer();
    }

    @Test
    void givenInputOfValidTokensThenReturnTokenList() {
        String inputFilePath = "src/test/resources/pass0.json";

        List<Token> expectedTokens = List.of(Token.OPEN_BRACE, Token.CLOSED_BRACE);
        List<Token> tokens = lexer.lex(new File(inputFilePath));

        assertEquals(expectedTokens, tokens);
    }

    @Test
    void givenInputOfInvalidTokensThenReturnEmptyList() {
        String inputFilePath = "src/test/resources/fail0.json";

        List<Token> expectedTokens = List.of();
        List<Token> tokens = lexer.lex(new File(inputFilePath));

        assertEquals(expectedTokens, tokens);
    }
}

