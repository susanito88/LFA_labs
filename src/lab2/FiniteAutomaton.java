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
        for (Map<Character, Set<String>> stateTransitions : transitions.values()) {
            for (Set<String> transitionStates : stateTransitions.values()) {
                if (transitionStates.size() > 1) {
                    return false;
                }
            }
        }
        return true;
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
