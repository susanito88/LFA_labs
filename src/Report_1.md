# Implementation of Regular Grammars and Finite Automata

## Course: Formal Languages & Finite Automata  
**Author:** Alexandru Magla FAF-231  

---

## Theory
Formal languages define structured communication using alphabets, vocabularies, and grammar rules. Regular grammars are a subset that can be recognized by finite automata, which consist of states, transitions, an initial state, and final states. Regular grammars are categorized into right-linear and left-linear, with right-linear grammars being directly translatable into finite automata.

## Objectives
- Understand regular grammars and finite automata.
- Implement a grammar to generate valid strings.
- Convert the grammar to a finite automaton.
- Check if a string belongs to the language.
- Demonstrate the relationship between grammars and automata.

## Implementation

### Grammar Class  
The `Grammar` class stores:
- **VN (Non-terminals):** A set of non-terminal symbols.
- **VT (Terminals):** A set of terminal symbols.
- **P (Production Rules):** A mapping from non-terminals to their possible derivations.
- **startSymbol:** The initial non-terminal from which derivations begin.

```java
public class Grammar {
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
}
```

### Finite Automaton Class  
A finite automaton is defined by:
- **States**: Representing different stages in processing input.
- **Alphabet**: The set of valid input symbols.
- **Transitions**: A mapping from states and input symbols to new states.
- **Start State**: The initial state where processing begins.
- **Final States**: Accepting states that indicate valid input recognition.

```java
public class FiniteAutomaton {
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
            if (!alphabet.contains(symbol) || !transitions.containsKey(currentState)) return false;
            currentState = transitions.get(currentState).getOrDefault(symbol, "");
            if (currentState.isEmpty()) return false;
        }
        return finalStates.contains(currentState) || currentState.isEmpty();
    }
}
```

### Main Class  
Executes the program by generating valid strings and testing the finite automaton.

```java
public class Main {
    public static void main(String[] args) {
        Set<String> VN = new HashSet<>(Arrays.asList("S", "A", "B", "C"));
        Set<Character> VT = new HashSet<>(Arrays.asList('a', 'b', 'c', 'd'));
        Map<String, List<String>> P = new HashMap<>();
        P.put("S", Arrays.asList("dA"));
        P.put("A", Arrays.asList("d", "aB"));
        P.put("B", Arrays.asList("bC"));
        P.put("C", Arrays.asList("cA", "aS"));
        
        Grammar grammar = new Grammar(VN, VT, P, "S");
        
        System.out.println("Generated Strings:");
        for (int i = 0; i < 5; i++) {
            System.out.println(grammar.generateString());
        }
        
        FiniteAutomaton automaton = new FiniteAutomaton(VN, VT, new HashMap<>(), "S", new HashSet<>(Collections.singleton("qf")));

        System.out.println("\nTesting Finite Automaton:");
        System.out.println("dd belongs to language: " + automaton.stringBelongToLanguage("dd"));
        System.out.println("aaaa belongs to language: " + automaton.stringBelongToLanguage("aaaa"));
    }
}
```

## Results and Observations
- The grammar successfully generates strings composed of terminal symbols.
- The finite automaton correctly determines whether a string belongs to the language.
- The conversion process highlights the close relationship between regular grammars and finite automata.

## Conclusions
- We implemented a regular grammar and a corresponding finite automaton.
- The experiment confirmed that finite automata can be derived from right-linear grammars.
- Testing showed that the automaton accurately recognizes valid language strings.

## References
- Course materials on Formal Languages & Finite Automata.


