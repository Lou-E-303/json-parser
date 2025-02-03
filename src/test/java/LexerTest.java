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

    Token s = new Token(TokenType.CONTENT, 's');
    Token o = new Token(TokenType.CONTENT, 'o');
    Token m = new Token(TokenType.CONTENT, 'm');
    Token e = new Token(TokenType.CONTENT, 'e');
    Token K = new Token(TokenType.CONTENT, 'K');
    Token y = new Token(TokenType.CONTENT, 'y');
    Token S = new Token(TokenType.CONTENT, 'S');
    Token v = new Token(TokenType.CONTENT, 'v');
    Token a = new Token(TokenType.CONTENT, 'a');
    Token l = new Token(TokenType.CONTENT, 'l');
    Token u = new Token(TokenType.CONTENT, 'u');

    Token colon = new Token(TokenType.COLON, ':');
    Token whitespace = new Token(TokenType.CONTENT, ' ');
    Token openBrace = new Token(TokenType.OBJECT_OPENER, '{');
    Token closedBrace = new Token(TokenType.OBJECT_CLOSER, '}');
    Token openBracket = new Token(TokenType.ARRAY_OPENER, '[');
    Token closedBracket = new Token(TokenType.ARRAY_CLOSER, ']');
    Token quote = new Token(TokenType.QUOTE, '"');

    @BeforeEach
    void init() {
        lexer = new Lexer();
    }

    @Test
    void givenInputOfValidTokensThenReturnTokenList() {
        String inputFilePath = "src/test/resources/lexer_pass_validTokens.json";
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

    @Test
    void givenInputWithWhitespaceInsideAndOutsideQuotesThenReturnCorrectTokens() {
        String inputFilePath = "src/test/resources/pass_mixedWhitespace.json";

        ArrayList<Token> expectedTokens = new ArrayList<>(List.of(openBrace, quote, s, o, m, e, K, e, y, quote, colon, quote, S, o, m, e, whitespace, v, a, l, u, e, quote, closedBrace));
        ArrayList<Token> tokens = new ArrayList<>(lexer.lex(new File(inputFilePath)));

        assertThat(tokens).isEqualTo(expectedTokens);
    }
}

