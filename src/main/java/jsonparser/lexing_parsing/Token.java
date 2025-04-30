package jsonparser.lexing_parsing;

import java.util.LinkedHashMap;
import java.util.Map;

public record Token(TokenType type, Object value) {
    private static final int CACHE_SIZE = 1000;
    private static final Map<String, Token> CACHE = new LinkedHashMap<>(CACHE_SIZE, 0.75f, true) {
        @Override
        protected boolean removeEldestEntry(Map.Entry<String, Token> eldest) {
            return size() > CACHE_SIZE;
        }
    };

    public static Token of(TokenType type, Object value) {
        String key = type + ":" + value;
        return CACHE.computeIfAbsent(key, k -> new Token(type, value));
    }
}
