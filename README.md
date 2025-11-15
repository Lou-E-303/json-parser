<h1 align="center"><b>JsonJar: A finite state machine JSON parser implementation for Java</b></h1>

<p align="center">
<img src="src/resources/json-jar.png" alt="JsonJar" style="width: 50%;" />

# How it works

- [JsonLexer](https://github.com/Lou-E-303/json-parser/blob/master/src/main/java/jsonparser/lexing_parsing/JsonLexer.java)
- [JsonParser](https://github.com/Lou-E-303/json-parser/blob/master/src/main/java/jsonparser/lexing_parsing/JsonParser.java)
- [JsonFiniteStateMachine](https://github.com/Lou-E-303/json-parser/blob/master/src/main/java/jsonparser/state_management/JsonFiniteStateMachine.java)

are the three key components to understanding how the parsing works.

From there, you can see the main logical flow of the parser, which parses lexical tokens using a finite state machine implementation:

1. The lexer breaks the input string down into lexical tokens
2. The tokens are then processed by the parser according to formal JSON grammar rules
3. The finite state machine manages the parse state transitions.

Everything else in this repo is utility built to support these three components.
# Run

- Coming soon!
# Links

Link to challenge [here](https://codingchallenges.fyi/challenges/challenge-json-parser/) with thanks to John Crickett.