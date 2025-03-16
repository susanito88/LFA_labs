package lab2;

import java.util.*;

public class Grammar {
    private Set<String> VN;
    private Set<Character> VT;
    private Map<String, List<String>> P;
    private String startSymbol;

    public Grammar() {
        this.VN = new HashSet<>(Arrays.asList("S", "A", "B", "C"));
        this.VT = new HashSet<>(Arrays.asList('a', 'b'));
        this.P = new HashMap<>();
        this.P.put("S", Arrays.asList("aA"));
        this.P.put("A", Arrays.asList("bS", "aB"));
        this.P.put("B", Arrays.asList("bC", "aB"));
        this.P.put("C", Arrays.asList("aA", "b"));
        this.startSymbol = "S";
    }

    public String generateString() {
        Random random = new Random();
        StringBuilder string = new StringBuilder(startSymbol);

        while (string.chars().anyMatch(c -> VN.contains(String.valueOf((char) c)))) {
            for (int i = 0; i < string.length(); i++) {
                String v = String.valueOf(string.charAt(i));
                if (VN.contains(v)) {
                    String replacement = P.get(v).get(random.nextInt(P.get(v).size()));
                    string.replace(i, i + 1, replacement);
                    break;
                }
            }
        }
        return string.toString();
    }

    public String classifyGrammar() {
        boolean isRegular = true, isContextFree = true, isContextSensitive = true;

        for (Map.Entry<String, List<String>> entry : P.entrySet()) {
            String lhs = entry.getKey();
            for (String rhs : entry.getValue()) {
                if (lhs.length() > rhs.length()) {
                    isContextSensitive = false;
                }
                if (lhs.length() != 1 || !VN.contains(lhs)) {
                    isContextFree = false;
                }
                if (rhs.length() > 2 || (rhs.length() == 2 && !(Character.isLowerCase(rhs.charAt(0)) && VN.contains(String.valueOf(rhs.charAt(1)))))) {
                    isRegular = false;
                }

            }
        }

        if (isRegular) return "Type 3: Regular Grammar";
        if (isContextFree) return "Type 2: Context-Free Grammar";
        if (isContextSensitive) return "Type 1: Context-Sensitive Grammar";
        return "Type 0: Unrestricted Grammar";
    }

    public FiniteAutomaton toFiniteAutomaton() {
        Set<String> q = new HashSet<>(VN);
        Set<Character> sigma = new HashSet<>(VT);
        Map<String, Map<Character, Set<String>>> delta = new HashMap<>();

        for (String key : P.keySet()) {
            delta.putIfAbsent(key, new HashMap<>());
            for (String rule : P.get(key)) {
                char symbol = rule.charAt(0);
                String nextState = rule.length() > 1 ? rule.substring(1) : null;

                delta.get(key).putIfAbsent(symbol, new HashSet<>());
                delta.get(key).get(symbol).add(nextState);
            }
        }

        Set<String> f = new HashSet<>();
        for (String key : P.keySet()) {
            if (P.get(key).stream().allMatch(r -> r.length() == 1 && VT.contains(r.charAt(0)))) {
                f.add(key);
            }
        }
        f.add("C");

        return new FiniteAutomaton(q, sigma, delta, startSymbol, f);
    }

    public Map<String, List<String>> finiteAutomatonToGrammar(FiniteAutomaton fa) {
        Map<String, List<String>> grammarRules = new HashMap<>();
        Map<String, String> stateMapping = new HashMap<>();

        List<String> states = new ArrayList<>(fa.getStates());
        if (states.size() >= 1) stateMapping.put(states.get(0), "S");
        if (states.size() >= 2) stateMapping.put(states.get(1), "A");
        if (states.size() >= 3) stateMapping.put(states.get(2), "B");
        if (states.size() >= 4) stateMapping.put(states.get(3), "C");

        for (String state : states) {
            String newStateName = stateMapping.getOrDefault(state, "");
            if (!newStateName.isEmpty()) {
                grammarRules.putIfAbsent(newStateName, new ArrayList<>());

                if (fa.getFinalStates().contains(state)) {
                    grammarRules.get(newStateName).add("ε");
                }

                if (fa.getTransitions().containsKey(state)) {
                    for (var entry : fa.getTransitions().get(state).entrySet()) {
                        char symbol = entry.getKey();
                        for (String nextState : entry.getValue()) {
                            String mappedNextState = stateMapping.getOrDefault(nextState, "");
                            grammarRules.get(newStateName).add(symbol + mappedNextState);
                        }
                    }
                }
            }
        }

        return grammarRules;
    }
}
