/**
 * @ author Markus Bowie
 * @ author Noël Hennings
 */
package prop.assignment0;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Tokenizer implements ITokenizer {

    private static Map<Character, Token> symbols = null;
    private static HashSet<String> ints = null;
    private static HashSet<String> ids = null;

    private Scanner scanner = null;
    private Lexeme current = null;
    private Lexeme next = null;

    public Tokenizer() {
        symbols = new HashMap<Character, Token>();
        ints = new HashSet<>();
        ids = new HashSet<>();

        symbols.put(Scanner.NULL, Token.NULL);
        symbols.put('+', Token.ADD_OP);
        symbols.put('-', Token.SUB_OP);
        symbols.put('*', Token.MULT_OP);
        symbols.put('/', Token.DIV_OP);
        symbols.put('=', Token.ASSIGN_OP);
        symbols.put('(', Token.LEFT_PAREN);
        symbols.put(')', Token.RIGHT_PAREN);
        symbols.put(';', Token.SEMICOLON);
        symbols.put('{', Token.LEFT_CURLY);
        symbols.put('}', Token.RIGHT_CURLY);
        symbols.put(Scanner.EOF, Token.EOF);

        for (int i = 0; i < 10; i++) {
            String j = Integer.toString(i);
            ints.add(j);
        }

        //System.out.println(ints);

        ids.add("a");
        ids.add("b");
        ids.add("c");
        ids.add("d");
        ids.add("e");
        ids.add("f");
        ids.add("g");
        ids.add("h");
        ids.add("i");
        ids.add("j");
        ids.add("k");
        ids.add("l");
        ids.add("m");
        ids.add("n");
        ids.add("o");
        ids.add("p");
        ids.add("q");
        ids.add("r");
        ids.add("s");
        ids.add("t");
        ids.add("u");
        ids.add("v");
        ids.add("w");
        ids.add("x");
        ids.add("y");
        ids.add("z");
    }


//    @Override
    public void open(String fileName) throws IOException, TokenizerException {
        scanner = new Scanner();
        scanner.open(fileName);
        scanner.moveNext();
        next = extractLexeme();
    }

//    @Override
    public Lexeme current() {
        return current;
    }

//    @Override
    public void moveNext() throws IOException, TokenizerException {
        if (scanner == null)
            throw new IOException("No open file.");
        current = next;
        if (next.token() != Token.EOF)
            next = extractLexeme();
    }

    private void consumeWhiteSpaces() throws IOException {
        while (Character.isWhitespace(scanner.current())) {
            scanner.moveNext();
        }
    }

//     fixar senare

    private Lexeme extractLexeme() throws IOException, TokenizerException {
        consumeWhiteSpaces();

        Character ch = scanner.current();
        StringBuilder strBuilder = new StringBuilder();

        if (ch == Scanner.EOF)
            return new Lexeme(ch, Token.EOF);
        else if (Character.isLetter(ch) || Character.isDigit(ch)) {
            while (Character.isLetter(scanner.current()) || Character.isDigit(scanner.current())) {
                strBuilder.append(scanner.current());
                scanner.moveNext();
            }
            String lexeme = strBuilder.toString();
            System.out.println(lexeme);

            if (ints.contains(lexeme)) {
                return new Lexeme(lexeme, Token.INT_LIT);
            } else if (ids.contains(lexeme)) {
                return new Lexeme(lexeme, Token.IDENT);
            } else throw new TokenizerException("Unknown lexeme: " + strBuilder.toString());
        } else if (symbols.containsKey(ch)) {
            scanner.moveNext();
            return new Lexeme(ch, symbols.get(ch));
        } else
            throw new TokenizerException("Unknown character: " + ch);
    }

//    @Override
    public void close() throws IOException {
        if (scanner != null)
            scanner.close();
    }
}


