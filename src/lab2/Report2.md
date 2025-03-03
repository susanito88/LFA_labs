# **Determinism in Finite Automata. Conversion from NDFA to DFA. Chomsky Hierarchy**

## **Course:** Formal Languages & Finite Automata  
### **Author:** Alexandru Magla | FAF-231 | Variant 20  

---

## **Theory**

### **Finite Automata**
Automata theory is the study of abstract computational devices (abstract state machines). An automaton serves as a model for digital computation, containing essential components:

- A mechanism for reading input (assumed to be a string over a given alphabet).
- A control unit with a finite number of states.
- A transition function dictating state changes.
- An output mechanism.

Finite automata (FA) are classified into different types:

- **Deterministic Finite Automaton (DFA)** – Each input symbol transitions to exactly one state.
- **Nondeterministic Finite Automaton (NFA)** – An input symbol can transition to multiple states, with possible ε (empty string) moves.
- **ε-Nondeterministic Finite Automaton (ε–NFA)** – Allows ε-transitions.

### **Deterministic Finite Automaton (DFA)**
A DFA is defined as a 5-tuple **(Q, Σ, δ, q₀, F)** where:

- **Q**: Finite set of states.
- **Σ**: Input alphabet.
- **δ**: Transition function, **δ: Q × Σ → Q**.
- **q₀**: Initial state.
- **F**: Set of final states.

### **Nondeterministic Finite Automaton (NFA)**
An NFA is defined as a 5-tuple **(Q, Σ, δ, q₀, F)** where:

- **Q**: Finite set of states.
- **Σ**: Input alphabet.
- **δ**: Transition function, **δ: Q × Σ → 2^Q**.
- **q₀**: Initial state.
- **F**: Set of final states.

**Key Differences from DFA:**
- The transition function **δ** can lead to multiple states.
- Allows ε-transitions.

### **Chomsky Hierarchy**
The Chomsky hierarchy classifies grammars based on complexity:

- **Type 0**: Recursively enumerable grammar.
- **Type 1**: Context-sensitive grammar.
- **Type 2**: Context-free grammar.
- **Type 3**: Regular grammar (right-linear or left-linear).

---

## **Objectives**
1. Understand automata and their applications.
2. Implement a function to classify grammar based on Chomsky hierarchy.
3. Given a finite automaton definition, perform the following tasks:
   - Convert an FA to a regular grammar.
   - Determine if an FA is deterministic or non-deterministic.
   - Convert an NDFA to a DFA.

---

## **Implementation Description**

### **Classifying Grammar Based on Chomsky Hierarchy**
To determine the type of grammar, the `classifyGrammar` method inspects production rules and classifies them accordingly:

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
This method converts finite automaton transitions into equivalent grammar rules:

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

---

## **Conclusion**
This lab provided a deeper understanding of automata and formal languages through the implementation of:
- Grammar classification based on Chomsky hierarchy.
- Conversion of FA to grammar.
- Conversion of NDFA to DFA.
These concepts play a crucial role in computational theory and compiler design.

