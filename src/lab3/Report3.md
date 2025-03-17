# **Lexer & Scanner**

## **Course:** Formal Languages & Finite Automata
### **Author:** Alexandru Magla

---

## **Theory**

### **Lexical Analysis**
Lexical analysis is the process of converting a sequence of characters (source code) into a sequence of tokens. This step is a fundamental component of a compiler or interpreter. The component responsible for lexical analysis is known as a **lexer**, **scanner**, or **tokenizer**.

A lexer processes an input string and produces a stream of **lexemes**, which are then categorized into **tokens** based on the syntax of the programming language. Tokens typically represent keywords, identifiers, operators, numbers, and punctuation marks.

### **Lexemes vs Tokens**
- **Lexemes**: These are the raw substrings extracted from the input text based on defined rules (e.g., splitting by whitespace or symbols).
- **Tokens**: These provide meaning to the lexemes by categorizing them into recognizable language constructs (e.g., keywords, identifiers, literals).

For example, given the input:
```plaintext
sin(30) + cos(45) - 10.5 * 2
```
The lexer might produce the following tokens:
```plaintext
Token(SIN, "sin"), Token(LPAREN, "("), Token(NUMBER, "30"), Token(RPAREN, ")"),
Token(PLUS, "+"), Token(COS, "cos"), Token(LPAREN, "("), Token(NUMBER, "45"),
Token(RPAREN, ")"), Token(MINUS, "-"), Token(NUMBER, "10.5"), Token(MULTIPLY, "*"), Token(NUMBER, "2")
```

---

## **Objectives**
1. Understand lexical analysis and tokenization.
2. Implement a lexer/scanner to tokenize an arithmetic expression with trigonometric functions.
3. Demonstrate how the lexer processes input and generates tokens.

---

## **Implementation Description**

### **Token Representation**
Each token consists of a **type** (category) and an optional **value**. The `Token` class represents this structure:
```java
public class Token {
    TokenType type;
    String value;
    
    public Token(TokenType type, String value) {
        this.type = type;
        this.value = value;
    }
    
    @Override
    public String toString() {
        return "Token{" + "type=" + type + ", value='" + value + "'}";
    }
}
```

### **Defining Token Types**
A set of predefined token categories is defined in `TokenType`:
```java
enum TokenType {
    NUMBER, SIN, COS, PLUS, MINUS, MULTIPLY, DIVIDE, LPAREN, RPAREN, EOF;
}
```

### **Lexer Implementation**
The `Lexer` class scans the input string using **regular expressions** to recognize token patterns:
```java
public class Lexer {
    private final String input;
    private int position = 0;
    
    private static final Pattern TOKEN_PATTERNS = Pattern.compile(
        "\\s*(?:(?<NUMBER>\\d+(?:\\.\\d+)?)|" +
        "(?<SIN>sin)|" +
        "(?<COS>cos)|" +
        "(?<PLUS>\\+)|" +
        "(?<MINUS>-)|" +
        "(?<MULTIPLY>\\*)|" +
        "(?<DIVIDE>/)|" +
        "(?<LPAREN>\\()|" +
        "(?<RPAREN>\\)))"
    );
    
    public Lexer(String input) {
        this.input = input;
    }
    
    public List<Token> tokenize() {
        List<Token> tokens = new ArrayList<>();
        Matcher matcher = TOKEN_PATTERNS.matcher(input);
        
        while (matcher.find()) {
            if (matcher.group("NUMBER") != null) {
                tokens.add(new Token(TokenType.NUMBER, matcher.group("NUMBER")));
            } else if (matcher.group("SIN") != null) {
                tokens.add(new Token(TokenType.SIN, "sin"));
            } else if (matcher.group("COS") != null) {
                tokens.add(new Token(TokenType.COS, "cos"));
            } else if (matcher.group("PLUS") != null) {
                tokens.add(new Token(TokenType.PLUS, "+"));
            } else if (matcher.group("MINUS") != null) {
                tokens.add(new Token(TokenType.MINUS, "-"));
            } else if (matcher.group("MULTIPLY") != null) {
                tokens.add(new Token(TokenType.MULTIPLY, "*"));
            } else if (matcher.group("DIVIDE") != null) {
                tokens.add(new Token(TokenType.DIVIDE, "/"));
            } else if (matcher.group("LPAREN") != null) {
                tokens.add(new Token(TokenType.LPAREN, "("));
            } else if (matcher.group("RPAREN") != null) {
                tokens.add(new Token(TokenType.RPAREN, ")"));
            }
        }
        tokens.add(new Token(TokenType.EOF, ""));
        return tokens;
    }
}
```

### **Testing the Lexer**
The `LexerDemo` class demonstrates tokenization of an arithmetic expression:
```java
public class LexerDemo {
    public static void main(String[] args) {
        String input = "sin(30) + cos(45) - 10.5 * 2";
        Lexer lexer = new Lexer(input);
        List<Token> tokens = lexer.tokenize();
        
        for (Token token : tokens) {
            System.out.println(token);
        }
    }
}
```

**Expected Output:**
```plaintext
Token{type=SIN, value='sin'}
Token{type=LPAREN, value='('}
Token{type=NUMBER, value='30'}
Token{type=RPAREN, value=')'}
Token{type=PLUS, value='+'}
Token{type=COS, value='cos'}
Token{type=LPAREN, value='('}
Token{type=NUMBER, value='45'}
Token{type=RPAREN, value=')'}
Token{type=MINUS, value='-'}
Token{type=NUMBER, value='10.5'}
Token{type=MULTIPLY, value='*'}
Token{type=NUMBER, value='2'}
Token{type=EOF, value=''}
```

---

## **Conclusion**
This lab focused on implementing a simple **lexer** capable of recognizing arithmetic expressions with trigonometric functions. The key takeaways include:
- Understanding the difference between **lexemes** and **tokens**.
- Implementing a **tokenizer** using **regular expressions**.
- Demonstrating lexical analysis through **Java code**.

This implementation lays the groundwork for building more complex language processing tools such as **parsers** and **interpreters** in the future.

