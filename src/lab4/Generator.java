package lab4;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Generator {
    private final String expr;
    private final Random random;

    public Generator(String expr) {
        this.expr = expr;
        this.random = new Random();
    }

    public String generateString() {
        List<String> string = new ArrayList<>();
        int i = 0;

        while (i < expr.length()) {
            char ch = expr.charAt(i);

            switch (ch) {
                case '(' -> {
                    int groupEnd = expr.indexOf(")", i);
                    String groupContent = expr.substring(i + 1, groupEnd);
                    String selected = generateGroup(groupContent);
                    string.add(selected);
                    i = groupEnd;
                }
                case '*' -> {
                    String last = string.remove(string.size() - 1);
                    string.add(repeat(last, random.nextInt(6)));
                }
                case '+' -> {
                    String last = string.remove(string.size() - 1);
                    string.add(repeat(last, 1 + random.nextInt(5)));
                }
                case '^' -> {
                    int repeat = Character.getNumericValue(expr.charAt(i + 1));
                    String last = string.remove(string.size() - 1);
                    string.add(repeat(last, repeat));
                    i++;
                }
                case '?' -> {
                    String last = string.remove(string.size() - 1);
                    string.add(repeat(last, random.nextInt(2)));
                }
                case '{' -> {
                    if (i + 2 < expr.length() && expr.charAt(i + 2) == '}') {
                        int repeat = Character.getNumericValue(expr.charAt(i + 1));
                        String last = string.remove(string.size() - 1);
                        string.add(repeat(last, repeat));
                        i += 2;
                    }
                }
                case '|', ')', '}' -> {
                    // Ignore
                }
                default -> string.add(String.valueOf(ch));
            }

            i++;
        }

        return String.join(" ", string);
    }

    private String generateGroup(String groupExpr) {
        String[] options = groupExpr.split("\\|");
        return options[random.nextInt(options.length)];
    }

    private String repeat(String str, int times) {
        return str.repeat(times);
    }

    public void generateNStrings() {
        for (int i = 0; i < 5; i++) {
            System.out.println(generateString());
        }
    }

    public static void main(String[] args) {
        Generator gen = new Generator("a(b|c)+d*e?f^2g{3}");
        gen.generateNStrings();
    }
}
