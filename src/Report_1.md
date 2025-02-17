# Implementation of Regular Grammars and Finite Automata

## Course: Formal Languages & Finite Automata  
**Author:** Alexandru Magla FAF-231  

---

## Theory
Formal languages define structured communication using alphabets, vocabularies, and grammar rules. Regular grammars are a subset that can be recognized by finite automata, which consist of states, transitions, an initial state, and final states.

## Objectives
- Understand regular grammars and finite automata.
- Implement a grammar to generate valid strings.
- Convert the grammar to a finite automaton.
- Check if a string belongs to the language.

## Implementation

### Grammar Class  
Stores non-terminal symbols (`VN`), terminal symbols (`VT`), and production rules (`P`).

```java
public class Grammar {
    private Set<String> VN;
    private Set<Character> VT;
    private Map<String, List<String>> P;
    private String startSymbol;
}
```

### Finite Automaton Class  
Checks if a string belongs to the language.

```java
public class FiniteAutomaton {
    public boolean stringBelongToLanguage(String inputString) {
        String currentState = startState;
        for (char symbol : inputString.toCharArray()) {
            if (!alphabet.contains(symbol) || !transitions.containsKey(currentState)) return false;
            currentState = transitions.get(currentState).getOrDefault(symbol, "");
            if (currentState.isEmpty()) return false;
        }
        return finalStates.contains(currentState) || currentState.isEmpty();
    }
}
```

### Main Class  
Executes the program by generating valid strings and testing the automaton.

```java
public class Main {
    public static void main(String[] args) {
        Grammar grammar = new Grammar(...);
        System.out.println(grammar.generateString());
    }
}
```

## Conclusions
- Implemented regular grammar and finite automaton.
- Successfully tested string generation and validation.

## References
- Course materials on Formal Languages & Finite Automata.
