package lexing_parsing;

import jsonparser.exceptions.JsonSyntaxException;
import jsonparser.lexing_parsing.TokenType;
import jsonparser.lexing_parsing.Token;
import jsonparser.lexing_parsing.JsonLexer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JsonLexerTest {
    private static JsonLexer lexer;

    Token someKey = Token.of(TokenType.CONTENT, "someKey");
    Token someWhitespaceValue = Token.of(TokenType.CONTENT, "Some value");
    Token key = Token.of(TokenType.CONTENT, "key");
    Token valueWithEscapedQuotes = Token.of(TokenType.CONTENT, "value with \"escaped quotes\"");

    Token colon = Token.of(TokenType.COLON, ':');
    Token openBrace = Token.of(TokenType.OBJECT_OPENER, '{');
    Token closedBrace = Token.of(TokenType.OBJECT_CLOSER, '}');
    Token openBracket = Token.of(TokenType.ARRAY_OPENER, '[');
    Token closedBracket = Token.of(TokenType.ARRAY_CLOSER, ']');
    Token theNumberThree = Token.of(TokenType.NUMBER, 3);
    Token oneTwoThreeFour = Token.of(TokenType.NUMBER, 1234);
    Token truthy = Token.of(TokenType.BOOLEAN, "true");
    Token falsy = Token.of(TokenType.BOOLEAN, "false");
    Token nully = Token.of(TokenType.NULL, "null");
    Token comma = Token.of(TokenType.COMMA, ',');

    @BeforeEach
    void init() {
        lexer = new JsonLexer();
    }

    @Test
    void givenInputOfValidTokensThenReturnTokenList() {
        String inputFilePath = "src/test/resources/lexer_validTokens.json";

        ArrayList<Token> expectedTokens = new ArrayList<>(
                List.of(openBrace,
                        key, colon,
                            openBracket,
                                someKey, comma,
                                theNumberThree, comma,
                                truthy, comma,
                                falsy, comma,
                                nully,
                            closedBracket,
                        closedBrace));
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
                someKey,
                colon,
                someWhitespaceValue,
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
                key,
                colon,
                valueWithEscapedQuotes,
                closedBrace
        ));

        ArrayList<Token> tokens = new ArrayList<>(lexer.lex(new File(inputFilePath)));

        assertThat(tokens).isEqualTo(expectedTokens);
    }

    @Test
    void givenRawTextInputShouldReportInvalidJson() {
        File invalid = new File("src/test/resources/fail_invalid.json");

        JsonSyntaxException exception = assertThrows(JsonSyntaxException.class,
                () -> lexer.lex(invalid));

        assertEquals("Error: invalid starting character 'I'", exception.getMessage());
    }

    @Test
    void givenRawTextInputWithNoKnownCharactersShouldReportInvalidJson() {
        File invalid = new File("src/test/resources/fail_NoKnownChars.json");

        JsonSyntaxException exception = assertThrows(JsonSyntaxException.class,
                () -> lexer.lex(invalid));

        assertEquals("Error: invalid starting character 'i'", exception.getMessage());
    }

    @Test
    void givenRawNumberInputShouldProduceCorrectTokens() {
        String inputFilePath = "src/test/resources/pass_singleNumber.json";

        ArrayList<Token> expectedTokens = new ArrayList<>(List.of(theNumberThree));
        ArrayList<Token> tokens = new ArrayList<>(lexer.lex(new File(inputFilePath)));

        assertThat(tokens).isEqualTo(expectedTokens);
    }

    @Test
    void givenLongRawNumberInputShouldProduceCorrectTokens() {
        String inputFilePath = "src/test/resources/pass_longNumber.json";

        ArrayList<Token> expectedTokens = new ArrayList<>(List.of(oneTwoThreeFour));
        ArrayList<Token> tokens = new ArrayList<>(lexer.lex(new File(inputFilePath)));

        assertThat(tokens).isEqualTo(expectedTokens);
    }

    @ParameterizedTest
    @MethodSource("numberAsStringInputs")
    void givenIncreasinglyComplexNumbersAsStringsShouldProduceCorrectTokens(String inputFilePath, Token inputToken) {
        ArrayList<Token> expectedTokens = new ArrayList<>(List.of(inputToken));
        ArrayList<Token> tokens = new ArrayList<>(lexer.lex(new File(inputFilePath)));

        assertThat(tokens).isEqualTo(expectedTokens);
    }

    static Stream<Arguments> numberAsStringInputs() {
        return Stream.of(
                Arguments.of("src/test/resources/pass_decimalNumber.json", Token.of(TokenType.NUMBER, "3.14")),
                Arguments.of("src/test/resources/pass_negativeNumber.json", Token.of(TokenType.NUMBER, "-42")),
                Arguments.of("src/test/resources/pass_scientificNotation.json", Token.of(TokenType.NUMBER, "123e4")),
                Arguments.of("src/test/resources/pass_complicatedNumber.json", Token.of(TokenType.NUMBER, "-123.456e10"))
        );
    }
}

