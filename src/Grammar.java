
import java.util.*;

class Grammar {
    private Set<String> VN;
    private Set<Character> VT;
    private Map<String, List<String>> P;
    private String startSymbol;

    public Grammar(Set<String> VN, Set<Character> VT, Map<String, List<String>> P, String startSymbol) {
        this.VN = VN;
        this.VT = VT;
        this.P = P;
        this.startSymbol = startSymbol;
    }

    public String generateString() {
        StringBuilder result = new StringBuilder();
        String currentSymbol = startSymbol;
        Random random = new Random();

        while (VN.contains(currentSymbol)) {
            List<String> possibleProductions = P.get(currentSymbol);
            if (possibleProductions == null || possibleProductions.isEmpty()) break;

            String production = possibleProductions.get(random.nextInt(possibleProductions.size()));
            result.append(production.replaceAll("[A-Z]", ""));
            currentSymbol = production.replaceAll("[^A-Z]", "");
        }

        return result.toString();
    }

    public FiniteAutomaton toFiniteAutomaton() {
        Set<String> states = new HashSet<>(VN);
        states.add("qf"); // Final state
        Set<Character> alphabet = new HashSet<>(VT);
        Map<String, Map<Character, String>> transitions = new HashMap<>();
        String startState = startSymbol;
        Set<String> finalStates = new HashSet<>();
        finalStates.add("qf");

        for (String nonTerminal : P.keySet()) {
            for (String rule : P.get(nonTerminal)) {
                char terminal = rule.charAt(0); // First character is the terminal symbol
                String nextState = rule.length() > 1 ? rule.substring(1) : "qf";

                transitions.putIfAbsent(nonTerminal, new HashMap<>());
                transitions.get(nonTerminal).put(terminal, nextState);
            }
        }

        return new FiniteAutomaton(states, alphabet, transitions, startState, finalStates);
    }

}