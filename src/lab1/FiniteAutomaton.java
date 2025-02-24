package lab1;

import java.util.*;

class FiniteAutomaton {
    private Set<String> states;
    private Set<Character> alphabet;
    private Map<String, Map<Character, String>> transitions;
    private String startState;
    private Set<String> finalStates;

    public FiniteAutomaton(Set<String> states, Set<Character> alphabet, Map<String, Map<Character, String>> transitions, String startState, Set<String> finalStates) {
        this.states = states;
        this.alphabet = alphabet;
        this.transitions = transitions;
        this.startState = startState;
        this.finalStates = finalStates;
    }

    public boolean stringBelongToLanguage(String inputString) {
        String currentState = startState;

        for (char symbol : inputString.toCharArray()) {
            if (!alphabet.contains(symbol) || !transitions.containsKey(currentState)) {
                return false;
            }
            Map<Character, String> possibleTransitions = transitions.get(currentState);

            if (!possibleTransitions.containsKey(symbol)) {
                return false;
            }

            currentState = possibleTransitions.get(symbol);
        }

        return finalStates.contains(currentState);
    }

}