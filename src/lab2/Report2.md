# Determinism in Finite Automata. Conversion from NDFA to DFA. Chomsky Hierarchy.

### Course: Formal Languages & Finite Automata

### Author: Alexandru Magla | FAF-231 | Variant 20

----

## Theory

### Finite Automata

Automata theory is the study of abstract computational devices (abstract state machines). An automaton is an abstract model of a digital computer. Every automaton includes essential features: it has a mechanism for reading input (assumed to be a string over a given alphabet), produces output in some form, and contains a control unit that can be in a finite number of states. The control unit changes state based on defined transition functions.

The finite automaton (FA) is characterized by a finite number of states and has the following types:

- **Deterministic Finite Automaton (DFA)** - each input symbol leads to exactly one state.
- **Nondeterministic Finite Automaton (NFA)** - an input symbol can lead to multiple states, and transitions can include ε (empty string) moves.
- **ε-Nondeterministic Finite Automaton (ε–NFA)** - allows ε-transitions.

### Deterministic Finite Automaton (DFA)
A deterministic finite automaton is a 5-tuple \( Q, \Sigma, \delta, q_0, F \) where:

- \( Q \) is a finite set of states.
- \( \Sigma \) is an input alphabet.
- \( \delta \) is a transition function, \( \delta: Q \times \Sigma \to Q \).
- \( q_0 \) is the initial state.
- \( F \) is a set of final states.

### Nondeterministic Finite Automaton (NFA)
A nondeterministic finite automaton is a 5-tuple \( Q, \Sigma, \delta, q_0, F \) where:

- \( Q \) is a finite set of states.
- \( \Sigma \) is an input alphabet.
- \( \delta \) is a transition function, \( \delta: Q \times \Sigma \to 2^Q \).
- \( q_0 \) is the initial state.
- \( F \) is a set of final states.

**Differences from the DFA:**
- The transition function \( \delta \) can go into multiple states.
- It can have ε-transitions.

### Chomsky Hierarchy
The Chomsky hierarchy classifies formal grammars into four types based on complexity:

- **Type 0**: Recursively enumerable grammar.
- **Type 1**: Context-sensitive grammar.
- **Type 2**: Context-free grammar.
- **Type 3**: Regular grammar (right-linear or left-linear).

## Objectives:
1. Understand automata and their applications.
2. Implement a function in the Grammar class to classify the grammar based on Chomsky hierarchy.
3. Given a finite automaton definition, complete the following tasks:
   - Convert a finite automaton to a regular grammar.
   - Determine whether the FA is deterministic or non-deterministic.
   - Implement functionality to convert an NDFA to a DFA.

## Implementation Description

### **Classifying Grammar Based on Chomsky Hierarchy**
To determine the type of grammar, the `classifyGrammar` method inspects the production rules and classifies them accordingly:

```java
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
            if (rhs.length() > 2 || (rhs.length() == 2 && !Character.isLowerCase(rhs.charAt(0)))) {
                isRegular = false;
            }
        }
    }
    
    if (isRegular) return "Type 3: Regular Grammar";
    if (isContextFree) return "Type 2: Context-Free Grammar";
    if (isContextSensitive) return "Type 1: Context-Sensitive Grammar";
    return "Type 0: Unrestricted Grammar";
}
```

### **Converting Finite Automaton to Regular Grammar**
The `finiteAutomatonToGrammar` method converts finite automaton transitions into equivalent grammar rules.

```java
public Map<String, List<String>> finiteAutomatonToGrammar(FiniteAutomaton fa) {
    Map<String, List<String>> grammarRules = new HashMap<>();
    
    for (String state : fa.getStates()) {
        grammarRules.putIfAbsent(state, new ArrayList<>());
        if (fa.getFinalStates().contains(state)) {
            grammarRules.get(state).add("ε");
        }
        
        for (char symbol : fa.getAlphabet()) {
            if (fa.getTransitions().containsKey(state) && fa.getTransitions().get(state).containsKey(symbol)) {
                for (String nextState : fa.getTransitions().get(state).get(symbol)) {
                    grammarRules.get(state).add(symbol + nextState);
                }
            }
        }
    }
    return grammarRules;
}
```

### **Checking if FA is Deterministic**
```java
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
```

### **Converting NDFA to DFA**
```java
public FiniteAutomaton convertToDFA() {
    Map<Set<String>, String> dfaStateMapping = new HashMap<>();
    Queue<Set<String>> queue = new LinkedList<>();
    Set<String> dfaStates = new HashSet<>();
    Map<String, Map<Character, String>> dfaTransitions = new HashMap<>();
    Set<String> dfaFinalStates = new HashSet<>();

    Set<String> startSet = new HashSet<>(Collections.singleton(startState));
    queue.add(startSet);
    dfaStateMapping.put(startSet, startState);
    dfaStates.add(startState);

    while (!queue.isEmpty()) {
        Set<String> currentSet = queue.poll();
        String currentDfaState = dfaStateMapping.get(currentSet);
        dfaTransitions.putIfAbsent(currentDfaState, new HashMap<>());

        for (char symbol : alphabet) {
            Set<String> newStateSet = new HashSet<>();
            for (String state : currentSet) {
                if (transitions.containsKey(state) && transitions.get(state).containsKey(symbol)) {
                    newStateSet.addAll(transitions.get(state).get(symbol));
                }
            }
            if (!newStateSet.isEmpty()) {
                String newState = dfaStateMapping.computeIfAbsent(newStateSet, s -> "q" + dfaStateMapping.size());
                dfaStates.add(newState);
                dfaTransitions.get(currentDfaState).put(symbol, newState);
                queue.add(newStateSet);
            }
        }
    }
    return new FiniteAutomaton(dfaStates, alphabet, convertTransitions(dfaTransitions), startState, dfaFinalStates);
}
```

## **Conclusion**
Through this lab, we implemented key concepts in finite automata theory, including classification of grammars, conversion from FA to grammar, and NDFA to DFA conversion. These implementations deepened our understanding of automata and formal language theory.

