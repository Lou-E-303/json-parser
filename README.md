<h1 align="center"><b>JsonJar</b></h1>
<p align="center">
<img src="src/resources/json-jar.png" alt="JsonJar" style="width: 50%;" />
<h3 align="center">A JSON parser implementation for Java</h3>

[![](https://jitpack.io/v/Lou-E-303/jsonjar.svg?refresh=true)](https://jitpack.io/#Lou-E-303/jsonjar)

# Features

- JSON parsing from file or string using finite state machine implementation
- Pretty-printing of JSON content

# Interface

## Parsing

- `JsonParser.parseFromString()`: Parses valid JSON `String` objects to hierarchical `Json` objects.
- `JsonParser.parseFromFile()`: Parses valid JSON `File` objects to hierarchical `Json` objects.

Both will throw an `IOException` in case of string or file read failure, and a `JsonReadException` in case of JSON read failure (e.g. due to invalid JSON input).

## Pretty-printing

- `JsonPrettyPrinter.getFormattedJsonString()`: Takes a `Json` object and returns a pretty-printed `String` object with proper indentation.

# How it works

- [JsonLexer](https://github.com/Lou-E-303/json-parser/blob/master/src/main/java/jsonparser/lexing_parsing/JsonLexer.java)
- [JsonParser](https://github.com/Lou-E-303/json-parser/blob/master/src/main/java/jsonparser/lexing_parsing/JsonParser.java)
- [JsonFiniteStateMachine](https://github.com/Lou-E-303/json-parser/blob/master/src/main/java/jsonparser/state_management/JsonFiniteStateMachine.java)

are the three key components to understanding how the parsing works.

From there, you can see the main logical flow of the parser, which parses lexical tokens using a finite state machine implementation:

1. The lexer breaks the input string down into lexical tokens
2. The tokens are then processed by the parser according to formal JSON grammar rules
3. The finite state machine manages the parse state transitions.

Everything else in this library is utility built to support these three components.

# Links

Link to challenge [here](https://codingchallenges.fyi/challenges/challenge-json-parser/) with thanks to John Crickett.