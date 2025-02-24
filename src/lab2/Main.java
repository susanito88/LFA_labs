package lab2;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Grammar grammar = new Grammar();

        System.out.println("Generated String: " + grammar.generateString());
        System.out.println("Grammar Classification: " + grammar.classifyGrammar());

        FiniteAutomaton fa = grammar.toFiniteAutomaton();
        System.out.println("Is the FA deterministic? " + fa.isDeterministic());

        Map<String, List<String>> faToGrammar = grammar.finiteAutomatonToGrammar(fa);
        System.out.println("\nFinite Automaton to Regular Grammar:");
        for (var entry : faToGrammar.entrySet()) {
            System.out.println(entry.getKey() + " -> " + String.join(", ", entry.getValue()));
        }
    }
}
