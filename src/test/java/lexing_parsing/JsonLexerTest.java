package lexing_parsing;

import jsonparser.error_handling.JsonReadException;
import jsonparser.error_handling.JsonSyntaxException;
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
    void givenFileInputOfValidTokensThenReturnTokenList() throws JsonReadException {
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
        ArrayList<Token> tokens = new ArrayList<>(lexer.lexFromFile(new File(inputFilePath)));

        assertThat(tokens).isEqualTo(expectedTokens);
    }

    @Test
    void givenSimpleJsonStringShouldProduceCorrectTokens() {
        String input = "{\"key\":\"value\"}";

        ArrayList<Token> expectedTokens = new ArrayList<>(List.of(
                openBrace,
                key,
                colon,
                Token.of(TokenType.CONTENT, "value"),
                closedBrace
        ));
        ArrayList<Token> tokens = new ArrayList<>(lexer.lexFromString(input));

        assertThat(tokens).isEqualTo(expectedTokens);
    }

    @Test
    void givenEmptyStringShouldReturnEmptyList() {
        String input = "";

        ArrayList<Token> expectedTokens = new ArrayList<>();
        ArrayList<Token> tokens = new ArrayList<>(lexer.lexFromString(input));

        assertThat(tokens).isEqualTo(expectedTokens);
    }

    @Test
    void givenNumberStringShouldProduceCorrectToken() {
        String input = "42";

        ArrayList<Token> expectedTokens = new ArrayList<>(List.of(
                Token.of(TokenType.NUMBER, "42")
        ));
        ArrayList<Token> tokens = new ArrayList<>(lexer.lexFromString(input));

        assertThat(tokens).isEqualTo(expectedTokens);
    }

    @Test
    void givenBooleanAndNullStringShouldProduceCorrectTokens() {
        String input = "[true,false,null]";

        ArrayList<Token> expectedTokens = new ArrayList<>(List.of(
                openBracket,
                truthy,
                comma,
                falsy,
                comma,
                nully,
                closedBracket
        ));
        ArrayList<Token> tokens = new ArrayList<>(lexer.lexFromString(input));

        assertThat(tokens).isEqualTo(expectedTokens);
    }

    @Test
    void givenEmptyFileInputThenReturnEmptyList() throws JsonReadException {
        String inputFilePath = "src/test/resources/fail_empty.json";

        ArrayList<Token> expectedTokens = new ArrayList<>(List.of());
        ArrayList<Token> tokens = new ArrayList<>(lexer.lexFromFile(new File(inputFilePath)));

        assertThat(tokens).isEqualTo(expectedTokens);
    }

    @Test
    void givenFileInputWithWhitespaceInsideAndOutsideQuotesThenReturnCorrectTokens() throws JsonReadException {
        String inputFilePath = "src/test/resources/pass_mixedWhitespace.json";

        ArrayList<Token> expectedTokens = new ArrayList<>(List.of(
                openBrace,
                someKey,
                colon,
                someWhitespaceValue,
                closedBrace
        ));
        ArrayList<Token> tokens = new ArrayList<>(lexer.lexFromFile(new File(inputFilePath)));

        assertThat(tokens).isEqualTo(expectedTokens);
    }

    @Test
    void givenFileInputWithEscapedQuotesThenReturnCorrectTokens() throws JsonReadException {
        String inputFilePath = "src/test/resources/lexer_escapedQuotes.json";

        ArrayList<Token> expectedTokens = new ArrayList<>(List.of(
                openBrace,
                key,
                colon,
                valueWithEscapedQuotes,
                closedBrace
        ));

        ArrayList<Token> tokens = new ArrayList<>(lexer.lexFromFile(new File(inputFilePath)));

        assertThat(tokens).isEqualTo(expectedTokens);
    }

    @Test
    void givenInvalidEscapeCharacterThenReportInvalidJson() {
        String inputFilePath = "src/test/resources/fail_invalidEscapeCharacter.json";
        File invalid = new File(inputFilePath);

        JsonSyntaxException exception = assertThrows(JsonSyntaxException.class,
                () -> lexer.lexFromFile(invalid));

        assertEquals("Error: Invalid escape character '\\x'", exception.getMessage());
    }

    @Test
    void givenRawNumberInputShouldProduceCorrectTokens() throws JsonReadException {
        String inputFilePath = "src/test/resources/pass_singleNumber.json";

        ArrayList<Token> expectedTokens = new ArrayList<>(List.of(theNumberThree));
        ArrayList<Token> tokens = new ArrayList<>(lexer.lexFromFile(new File(inputFilePath)));

        assertThat(tokens).isEqualTo(expectedTokens);
    }

    @Test
    void givenLongRawNumberInputShouldProduceCorrectTokens() throws JsonReadException {
        String inputFilePath = "src/test/resources/pass_longNumber.json";

        ArrayList<Token> expectedTokens = new ArrayList<>(List.of(oneTwoThreeFour));
        ArrayList<Token> tokens = new ArrayList<>(lexer.lexFromFile(new File(inputFilePath)));

        assertThat(tokens).isEqualTo(expectedTokens);
    }

    @ParameterizedTest
    @MethodSource("numberAsStringInputs")
    void givenIncreasinglyComplexNumbersAsStringsShouldProduceCorrectTokens(String inputFilePath, Token inputToken) throws JsonReadException {
        ArrayList<Token> expectedTokens = new ArrayList<>(List.of(inputToken));
        ArrayList<Token> tokens = new ArrayList<>(lexer.lexFromFile(new File(inputFilePath)));

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

    @ParameterizedTest
    @MethodSource("invalidNumberInputs")
    void givenInvalidNumberFormatsShouldReportInvalidJson(String inputFilePath, String expectedMessage) {
        File invalid = new File(inputFilePath);

        JsonSyntaxException exception = assertThrows(JsonSyntaxException.class,
                () -> lexer.lexFromFile(invalid));

        assertEquals(expectedMessage, exception.getMessage());
    }

    static Stream<Arguments> invalidNumberInputs() {
        return Stream.of(
                Arguments.of("src/test/resources/fail_numberWithLeadingZero.json", "Error: Numbers cannot have leading zeros."),
                Arguments.of("src/test/resources/fail_negativeNumberWithLeadingZero.json", "Error: Numbers cannot have leading zeros."),
                Arguments.of("src/test/resources/fail_NoKnownChars.json", "Error: invalid starting character 'i'"),
                Arguments.of("src/test/resources/fail_invalid.json", "Error: invalid starting character 'I'")
        );
    }
}

