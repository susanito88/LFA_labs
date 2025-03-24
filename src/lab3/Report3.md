# **Lexer & Scanner**

## **Course:** Formal Languages & Finite Automata
### **Author:** Alexandru Magla

---

## **Theory**

### **Lexical Analysis**
Lexical analysis is the first step in the compilation process, converting a sequence of characters (source code) into a sequence of tokens. This step is handled by a **lexer**, also called a **scanner** or **tokenizer**.

A lexer scans an input string and identifies **lexemes**, which are categorized into **tokens** based on language rules. Tokens represent elements like keywords, identifiers, operators, numbers, and punctuation.

For example, the input:
```plaintext
sin(30) + cos(45) - 10.5 * 2
```
Generates tokens:
```plaintext
Token(SIN, "sin"), Token(LPAREN, "("), Token(NUMBER, "30"), ...
```

---

## **Objectives**
- Understand lexical analysis and tokenization.
- Implement a lexer to tokenize an arithmetic expression with trigonometric functions.
- Demonstrate how the lexer processes input and generates tokens.

---

## **Implementation Description**

### **Token Representation**
A **token** consists of a **type** and an optional **value**:
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
A set of predefined **token categories**:
```java
enum TokenType {
    NUMBER, SIN, COS, PLUS, MINUS, MULTIPLY, DIVIDE, LPAREN, RPAREN, EOF;
}
```

### **Lexer Implementation**
The **Lexer** class scans an input string using **regular expressions**. It maintains a position pointer and matches patterns to extract tokens sequentially.

#### **Class Structure**
```java
public class Lexer {
    private final String input;
    private int position = 0;
    private static final Pattern TOKEN_PATTERNS = Pattern.compile(
        "\\s*(?:(?<NUMBER>\\d+(?:\\.\\d+)?)|" +
        "(?<SIN>sin)|(?<COS>cos)|(?<PLUS>\\+)|(?<MINUS>-)|" +
        "(?<MULTIPLY>\\*)|(?<DIVIDE>/)|(?<LPAREN>\\()|(?<RPAREN>\\)))"
    );

    public Lexer(String input) {
        this.input = input;
    }
```

#### **Tokenization Process**
The `tokenize()` method uses a `Matcher` to identify token patterns and extract matches.
```java
    public List<Token> tokenize() {
        List<Token> tokens = new ArrayList<>();
        Matcher matcher = TOKEN_PATTERNS.matcher(input);
        while (matcher.find()) {
            if (matcher.group("NUMBER") != null) tokens.add(new Token(TokenType.NUMBER, matcher.group("NUMBER")));
            else if (matcher.group("SIN") != null) tokens.add(new Token(TokenType.SIN, "sin"));
            else if (matcher.group("COS") != null) tokens.add(new Token(TokenType.COS, "cos"));
            else if (matcher.group("PLUS") != null) tokens.add(new Token(TokenType.PLUS, "+"));
            else if (matcher.group("MINUS") != null) tokens.add(new Token(TokenType.MINUS, "-"));
            else if (matcher.group("MULTIPLY") != null) tokens.add(new Token(TokenType.MULTIPLY, "*"));
            else if (matcher.group("DIVIDE") != null) tokens.add(new Token(TokenType.DIVIDE, "/"));
            else if (matcher.group("LPAREN") != null) tokens.add(new Token(TokenType.LPAREN, "("));
            else if (matcher.group("RPAREN") != null) tokens.add(new Token(TokenType.RPAREN, ")"));
        }
        tokens.add(new Token(TokenType.EOF, ""));
        return tokens;
    }
}
```

### **Handling Errors in Tokenization**
A lexer should be able to handle invalid characters gracefully. A simple way to do this is by adding an error-handling mechanism:
```java
    public List<Token> tokenize() {
        List<Token> tokens = new ArrayList<>();
        Matcher matcher = TOKEN_PATTERNS.matcher(input);
        while (matcher.find()) {
            String match = matcher.group();
            if (match.trim().isEmpty()) continue; // Ignore whitespace
            if (matcher.group("NUMBER") != null) tokens.add(new Token(TokenType.NUMBER, match));
            ...
            else throw new IllegalArgumentException("Unexpected character: " + match);
        }
        tokens.add(new Token(TokenType.EOF, ""));
        return tokens;
    }
```
This ensures that unrecognized characters raise an exception instead of producing incorrect results.

---

### **Testing the Lexer**
A simple **demo class** tests tokenization:
```java
public class LexerDemo {
    public static void main(String[] args) {
        Lexer lexer = new Lexer("sin(30) + cos(45) - 10.5 * 2");
        lexer.tokenize().forEach(System.out::println);
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
This implementation provides a simple lexer that recognizes arithmetic expressions containing trigonometric functions. By breaking down an input string into categorized tokens, we enable further processing for parsing and interpretation. The use of regular expressions allows efficient pattern matching for different token types. Error handling ensures robustness, making this a strong foundation for building more complex language processing tools like parsers and interpreters.

