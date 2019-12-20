package ru.mirea.lang;

import java.util.regex.Pattern;

public enum TokenType {
    ASSIGN(":="),
    TRUE("true"),
    FALSE("false"),
    AND("and"),
    OR("or"),
    XOR("xor"),
    PRINT("print"),
    NOT("not"),
    IDENT("[a-zA-Z]+[0-9]*"),
    SEMICOLON(";"),
    LPAR("\\("),
    RPAR("\\)"),
    SPACE("[ \t\r]+"),
    ENDL("\n");



    final Pattern pattern;

    TokenType(String regexp) {
        pattern = Pattern.compile(regexp);
    }
}
