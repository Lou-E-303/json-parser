import jsonparser.lexing_parsing.TokenType;
import jsonparser.lexing_parsing.Token;
import jsonparser.lexing_parsing.Lexer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class LexerTest {
    private static Lexer lexer;

    @BeforeAll
    static void init() {
        lexer = new Lexer();
    }

    @Test
    void givenInputOfValidTokensThenReturnTokenList() {
        String inputFilePath = "src/test/resources/lexer_pass_validTokens.json";

        Token openBrace = new Token(TokenType.OBJECT_OPENER, '{');
        Token closedBrace = new Token(TokenType.OBJECT_CLOSER, '}');
        Token openBracket = new Token(TokenType.ARRAY_OPENER, '[');
        Token closedBracket = new Token(TokenType.ARRAY_CLOSER, ']');
        Token quote = new Token(TokenType.QUOTE, '"');

        ArrayList<Token> expectedTokens = new ArrayList<>(List.of(openBrace, closedBrace, openBracket, closedBracket, quote));
        ArrayList<Token> tokens = new ArrayList<>(lexer.lex(new File(inputFilePath)));

        assertThat(tokens).isEqualTo(expectedTokens);
    }

    @Test
    void givenEmptyInputThenReturnEmptyList() {
        String inputFilePath = "src/test/resources/fail_empty.json";

        ArrayList<Token> expectedTokens = new ArrayList<>(List.of());
        ArrayList<Token> tokens = new ArrayList<>(lexer.lex(new File(inputFilePath)));

        assertThat(tokens).isEqualTo(expectedTokens);
    }
}

