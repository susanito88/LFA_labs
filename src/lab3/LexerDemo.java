package lab3;

import java.util.List;

public class LexerDemo {
    public static void main(String[] args) {
        String input = "sin(30) + cos(45) - 10.5 * 2";
        Lexer lexer = new Lexer(input);
        List<Token> tokens = lexer.tokenize();

        for (Token token : tokens) {
            System.out.println(token);
        }
    }
}