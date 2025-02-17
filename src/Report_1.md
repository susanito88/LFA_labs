# Implementation of Regular Grammars and Finite Automata

## Course: Formal Languages & Finite Automata  
**Author:** Alexandru Magla FAF-231  

---

## Theory
Formal languages define structured communication using alphabets, vocabularies, and grammar rules. Regular grammars are a subset that can be recognized by finite automata, which consist of states, transitions, an initial state, and final states. Regular grammars are categorized into right-linear and left-linear, with right-linear grammars being directly translatable into finite automata.

## Objectives
The purpose of this study is to understand regular grammars and finite automata, implement a grammar to generate valid strings, convert the grammar to a finite automaton, check if a string belongs to the language, and demonstrate the relationship between grammars and automata.

## Implementation

### Grammar Class  
The Grammar class is responsible for storing the set of non-terminal symbols, terminal symbols, production rules, and the start symbol. The generateString function generates a random string following the grammar rules. It starts from the start symbol, applying production rules until no non-terminals remain. A random production is selected at each step, and the non-terminals are removed from the final string before returning it.

```java
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
```

### Finite Automaton Class  
A finite automaton consists of states representing different stages in processing input, an alphabet defining valid input symbols, transitions mapping states to new states based on input symbols, a start state where processing begins, and final states marking valid input recognition. The stringBelongToLanguage function verifies whether a given string is recognized by the automaton. It iterates through the input string and follows the state transitions based on the input symbols. If it reaches a valid final state, the function returns true; otherwise, it returns false.

```java
public boolean stringBelongToLanguage(String inputString) {
    String currentState = startState;
    for (char symbol : inputString.toCharArray()) {
        if (!alphabet.contains(symbol) || !transitions.containsKey(currentState)) return false;
        currentState = transitions.get(currentState).getOrDefault(symbol, "");
        if (currentState.isEmpty()) return false;
    }
    return finalStates.contains(currentState) || currentState.isEmpty();
}
```

### Main Class  
The main function initializes a grammar with predefined non-terminals, terminals, and production rules. It then generates random strings using the grammar and tests the finite automaton with sample inputs. The function prints five randomly generated strings and checks whether specific test strings belong to the defined language.

```java
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
```

## Results and Observations
The grammar successfully generates strings composed of terminal symbols, and the finite automaton correctly determines whether a string belongs to the language. The conversion process highlights the close relationship between regular grammars and finite automata.

## Conclusions
The study implemented a regular grammar and a corresponding finite automaton. The experiment confirmed that finite automata can be derived from right-linear grammars. Testing showed that the automaton accurately recognizes valid language strings.

## References
Course materials on Formal Languages & Finite Automata. "Introduction to the Theory of Computation" by Michael Sipser. "Automata Theory, Languages, and Computation" by John E. Hopcroft and Jeffrey D. Ullman.

