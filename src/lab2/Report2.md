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
To determine the type of grammar, the `classifyGrammar` method inspects production rules and classifies them accordingly. This method iterates over the production rules of the grammar and checks specific constraints to determine its type. If all productions conform to the strictest rules, the grammar is classified as a regular grammar. If any condition for context-free, context-sensitive, or unrestricted grammars is met, it is classified accordingly. The method ensures efficiency by making use of boolean flags to minimize unnecessary operations.

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
    
```

### **Converting Finite Automaton to Regular Grammar**
This method converts a finite automaton into a set of production rules for a regular grammar. It first initializes an empty mapping of states to their associated rules. Then, for each state, it checks if it is a final state and adds an ε-transition to represent acceptance. It further iterates through each transition to construct the corresponding grammar rule by appending the transition symbol and next state. This approach ensures that the generated grammar accurately represents the automaton's behavior.

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
This function verifies whether a finite automaton is deterministic by checking if every state has at most one transition for each input symbol. It iterates through the transition table and ensures that no state has multiple transitions on the same symbol. If any state has more than one transition for a given symbol, the function immediately returns `false`, indicating nondeterminism. This approach allows for quick detection of non-deterministic behavior in the automaton.

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
This function converts a nondeterministic finite automaton (NDFA) into a deterministic finite automaton (DFA) by constructing new states that represent sets of NDFA states. It uses a queue to process states iteratively and a mapping to track newly created DFA states. The function systematically examines transitions for each NDFA state set, ensuring that the resulting DFA has a well-defined deterministic transition table. This approach provides an efficient way to handle nondeterministic state behavior and ensure an equivalent DFA representation.

```java
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
}
```

---

## **Conclusion**
This lab provided a deeper understanding of automata and formal languages through the implementation of:
- Grammar classification based on Chomsky hierarchy.
- Conversion of FA to grammar.
- Conversion of NDFA to DFA.
These concepts play a crucial role in computational theory and compiler design. The additional explanations and step-by-step breakdown of each method ensure a comprehensive understanding of automata transformations and their significance in computational models.

