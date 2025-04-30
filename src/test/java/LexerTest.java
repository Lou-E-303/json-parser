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

    Token someKey = Token.of(TokenType.CONTENT, "someKey");
    Token someWhitespaceValue = Token.of(TokenType.CONTENT, "Some value");
    Token key = Token.of(TokenType.CONTENT, "key");
    Token valueWithEscapedQuotes = Token.of(TokenType.CONTENT, "value with \"escaped quotes\"");

    Token colon = Token.of(TokenType.COLON, ':');
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
        String inputFilePath = "src/test/resources/lexer_validTokens.json";
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

        ArrayList<Token> expectedTokens = new ArrayList<>(List.of(
                openBrace,
                quote, someKey, quote,
                colon,
                quote, someWhitespaceValue, quote,
                closedBrace
        ));
        ArrayList<Token> tokens = new ArrayList<>(lexer.lex(new File(inputFilePath)));

        assertThat(tokens).isEqualTo(expectedTokens);
    }

    @Test
    void givenInputWithEscapedQuotesThenReturnCorrectTokens() {
        String inputFilePath = "src/test/resources/lexer_escapedQuotes.json";

        ArrayList<Token> expectedTokens = new ArrayList<>(List.of(
                openBrace,
                quote, key, quote,
                colon,
                quote, valueWithEscapedQuotes, quote,
                closedBrace
        ));

        ArrayList<Token> tokens = new ArrayList<>(lexer.lex(new File(inputFilePath)));

        assertThat(tokens).isEqualTo(expectedTokens);
    }
}

