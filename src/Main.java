import java.util.*;

public class Main {
    public static void main(String[] args) {
        Set<String> VN = new HashSet<>(Arrays.asList("S", "A", "B", "C"));
        Set<Character> VT = new HashSet<>(Arrays.asList('a', 'b', 'c', 'd'));
        Map<String, List<String>> P = new HashMap<>();
        P.put("S", Arrays.asList("dA"));
        P.put("A", Arrays.asList("d", "aB"));
        P.put("B", Arrays.asList("bC"));
        P.put("C", Arrays.asList("cA", "aS"));

        Grammar grammar = new Grammar(VN, VT, P, "S");

        System.out.println("Generated Strings:");
        for (int i = 0; i < 5; i++) {
            System.out.println(grammar.generateString());
        }

        FiniteAutomaton automaton = grammar.toFiniteAutomaton();

        System.out.println("\nTesting Finite Automaton:");
        System.out.println("dd belongs to language: " + automaton.stringBelongToLanguage("dd"));
        System.out.println("aaaa belongs to language: " + automaton.stringBelongToLanguage("aaaa"));
    }
}
