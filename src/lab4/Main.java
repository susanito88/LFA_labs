package lab4;

public class Main {
    public static void main(String[] args) {
        String[] expressions = {
                "(S|T)(U|V)W*Y+24",
                "L(M|N)O{3}P*Q(2|3)",
                "R*S(T|U|V)W(X|Y|Z){2}"
        };

        for (String expr : expressions) {
            System.out.println("\nGenerated strings for expression: " + expr);
            Generator generator = new Generator(expr);
            generator.generateNStrings();
        }
    }
}

