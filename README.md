# Intro

This is a JSON parser. As of right now, it's a work-in-progress and won't be fully-featured if cloned. I also might've just left it in a completely broken state!

# How it works

- `\src\main\java\jsonparser\lexing_parsing\JsonLexer.java`
- `\src\main\java\jsonparser\lexing_parsing\JsonParser.java`
- `\src\main\java\jsonparser\state_management\JsonFiniteStateMachine.java`

are the three key components to understanding how the parsing works.

From there, you can see the main logical flow of the parser, which is based on a finite state transition parsing technique:

1. The lexer breaks the input string down into lexical tokens
2. The tokens are then processed by the parser according to formal JSON grammar rules
3. The finite state machine manages the parse state transitions.

Everything else in this repo is utility built to support these three components.

# Run

- Coming soon!

# Links

Link to challenge [here](https://codingchallenges.fyi/challenges/challenge-json-parser/) with thanks to John Crickett.