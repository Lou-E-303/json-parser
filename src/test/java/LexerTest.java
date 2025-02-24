import jsonparser.lexing_parsing.TokenType;
import jsonparser.lexing_parsing.Token;
import jsonparser.lexing_parsing.Lexer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class LexerTest {
    private static Lexer lexer;

    Token s = Token.of(TokenType.CONTENT, 's');
    Token o = Token.of(TokenType.CONTENT, 'o');
    Token m = Token.of(TokenType.CONTENT, 'm');
    Token e = Token.of(TokenType.CONTENT, 'e');
    Token K = Token.of(TokenType.CONTENT, 'K');
    Token y = Token.of(TokenType.CONTENT, 'y');
    Token S = Token.of(TokenType.CONTENT, 'S');
    Token v = Token.of(TokenType.CONTENT, 'v');
    Token a = Token.of(TokenType.CONTENT, 'a');
    Token l = Token.of(TokenType.CONTENT, 'l');
    Token u = Token.of(TokenType.CONTENT, 'u');

    Token colon = Token.of(TokenType.COLON, ':');
    Token whitespace = Token.of(TokenType.CONTENT, ' ');
    Token openBrace = Token.of(TokenType.OBJECT_OPENER, '{');
    Token closedBrace = Token.of(TokenType.OBJECT_CLOSER, '}');
    Token openBracket = Token.of(TokenType.ARRAY_OPENER, '[');
    Token closedBracket = Token.of(TokenType.ARRAY_CLOSER, ']');
    Token quote = Token.of(TokenType.QUOTE, '"');

    @BeforeEach
    void init() {
        lexer = new Lexer();
    }

    @Test
    void givenInputOfValidTokensThenReturnTokenList() {
        String inputFilePath = "src/test/resources/lexer_pass_validTokens.json";
        Token quote = Token.of(TokenType.QUOTE, '"');

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

    @Test
    void givenInputWithWhitespaceInsideAndOutsideQuotesThenReturnCorrectTokens() {
        String inputFilePath = "src/test/resources/pass_mixedWhitespace.json";

        ArrayList<Token> expectedTokens = new ArrayList<>(List.of(openBrace, quote, s, o, m, e, K, e, y, quote, colon, quote, S, o, m, e, whitespace, v, a, l, u, e, quote, closedBrace));
        ArrayList<Token> tokens = new ArrayList<>(lexer.lex(new File(inputFilePath)));

        assertThat(tokens).isEqualTo(expectedTokens);
    }
}

