package ru.mirea.lang;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

public class Lexer {

    private final String src;
    private int pos = 0;
    private int row = 1;
    private int column = 1;
    private final List<Token> tokens = new ArrayList<>();

    public Lexer(String src) {
        this.src = src;
    }

    private boolean nextToken() {
        if (pos >= src.length())
            return false;

        for (TokenType tt : TokenType.values()) {
            Matcher m = tt.pattern.matcher(src);
            m.region(pos, src.length());
            if (m.lookingAt()) {
                Token t = new Token(tt, m.group(), pos, row, column);
                tokens.add(t);
                pos = m.end();

                if (tt == TokenType.ENDL) {
                    row++;
                    column = 1;
                }
                else
                    column += m.group().length();

                return true;
            }
        }
        throw new RuntimeException("Неожиданный символ " + src.charAt(pos) + " в позиции " + pos);
    }

    public List<Token> lex() {
        while (nextToken()) {
            // do nothing
        }
        return tokens;
    }

    public static void main(String[] args) {
        String text = "x := true;\n" +
                "y := not y;\n" +
                "print y;\n" +
                "z := (x and y) or not true;\n";
        Lexer l = new Lexer(text);
        List<Token> tokens = l.lex();
        for (Token t : tokens) {
            System.out.println(t.type + " " + t.text + " " + t.row + " " + t.column);
        }
    }
}
