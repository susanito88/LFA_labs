package lab2;

import java.util.*;

public class FiniteAutomaton {
    private Set<String> states;
    private Set<Character> alphabet;
    private Map<String, Map<Character, Set<String>>> transitions;
    private String startState;
    private Set<String> finalStates;

    public FiniteAutomaton(Set<String> q, Set<Character> sigma,
                           Map<String, Map<Character, Set<String>>> delta,
                           String q0, Set<String> f) {
        this.states = q;
        this.alphabet = sigma;
        this.transitions = delta;
        this.startState = q0;
        this.finalStates = f;
    }

    public boolean isDeterministic() {
        for (var stateTransitions : transitions.entrySet()) {
            for (var transition : stateTransitions.getValue().entrySet()) {
                if (transition.getValue().size() > 1) {
                    return false;
                }
            }
        }
        return true;
    }

    public FiniteAutomaton convertNdfaToDfa() {
        Map<Set<String>, String> dfaStates = new HashMap<>();
        Queue<Set<String>> queue = new LinkedList<>();
        Set<String> startSet = new HashSet<>(Collections.singleton(startState));
        dfaStates.put(startSet, "q0");
        queue.add(startSet);
        Map<String, Map<Character, String>> dfaTransitions = new HashMap<>();
        Set<String> dfaFinalStates = new HashSet<>();

        while (!queue.isEmpty()) {
            Set<String> currentSet = queue.poll();
            String dfaStateName = dfaStates.get(currentSet);
            dfaTransitions.putIfAbsent(dfaStateName, new HashMap<>());

            for (char symbol : alphabet) {
                Set<String> newSet = new HashSet<>();
                for (String state : currentSet) {
                    if (transitions.containsKey(state) && transitions.get(state).containsKey(symbol)) {
                        newSet.addAll(transitions.get(state).get(symbol));
                    }
                }
                if (!newSet.isEmpty()) {
                    dfaStates.putIfAbsent(newSet, "q" + dfaStates.size());
                    queue.add(newSet);
                    dfaTransitions.get(dfaStateName).put(symbol, dfaStates.get(newSet));
                }
            }
        }

        for (Set<String> nfaStateSet : dfaStates.keySet()) {
            for (String nfaState : nfaStateSet) {
                if (finalStates.contains(nfaState)) {
                    dfaFinalStates.add(dfaStates.get(nfaStateSet));
                    break;
                }
            }
        }

        return new FiniteAutomaton(new HashSet<>(dfaStates.values()), alphabet, transformTransitions(dfaTransitions), "q0", dfaFinalStates);
    }

    private Map<String, Map<Character, Set<String>>> transformTransitions(Map<String, Map<Character, String>> dfaTransitions) {
        Map<String, Map<Character, Set<String>>> transformed = new HashMap<>();
        for (var entry : dfaTransitions.entrySet()) {
            transformed.put(entry.getKey(), new HashMap<>());
            for (var subEntry : entry.getValue().entrySet()) {
                transformed.get(entry.getKey()).put(subEntry.getKey(), new HashSet<>(Collections.singleton(subEntry.getValue())));
            }
        }
        return transformed;
    }

    public Set<String> getStates() {
        return states;
    }

    public Set<Character> getAlphabet() {
        return alphabet;
    }

    public Map<String, Map<Character, Set<String>>> getTransitions() {
        return transitions;
    }

    public String getStartState() {
        return startState;
    }

    public Set<String> getFinalStates() {
        return finalStates;
    }
}
