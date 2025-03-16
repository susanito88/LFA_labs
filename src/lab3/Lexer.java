package lab3;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {
    private final String input;
    private int position = 0;

    private static final Pattern TOKEN_PATTERNS = Pattern.compile(
            "\\s*(?:(?<NUMBER>\\d+(?:\\.\\d+)?)|" +
                    "(?<SIN>sin)|" +
                    "(?<COS>cos)|" +
                    "(?<PLUS>\\+)|" +
                    "(?<MINUS>-)|" +
                    "(?<MULTIPLY>\\*)|" +
                    "(?<DIVIDE>/)|" +
                    "(?<LPAREN>\\()|" +
                    "(?<RPAREN>\\)))"
    );

    public Lexer(String input) {
        this.input = input;
    }

    public List<Token> tokenize() {
        List<Token> tokens = new ArrayList<>();
        Matcher matcher = TOKEN_PATTERNS.matcher(input);

        while (matcher.find()) {
            if (matcher.group("NUMBER") != null) {
                tokens.add(new Token(TokenType.NUMBER, matcher.group("NUMBER")));
            } else if (matcher.group("SIN") != null) {
                tokens.add(new Token(TokenType.SIN, "sin"));
            } else if (matcher.group("COS") != null) {
                tokens.add(new Token(TokenType.COS, "cos"));
            } else if (matcher.group("PLUS") != null) {
                tokens.add(new Token(TokenType.PLUS, "+"));
            } else if (matcher.group("MINUS") != null) {
                tokens.add(new Token(TokenType.MINUS, "-"));
            } else if (matcher.group("MULTIPLY") != null) {
                tokens.add(new Token(TokenType.MULTIPLY, "*"));
            } else if (matcher.group("DIVIDE") != null) {
                tokens.add(new Token(TokenType.DIVIDE, "/"));
            } else if (matcher.group("LPAREN") != null) {
                tokens.add(new Token(TokenType.LPAREN, "("));
            } else if (matcher.group("RPAREN") != null) {
                tokens.add(new Token(TokenType.RPAREN, ")"));
            }
        }
        tokens.add(new Token(TokenType.EOF, ""));
        return tokens;
    }
}

