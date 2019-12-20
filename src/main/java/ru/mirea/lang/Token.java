package ru.mirea.lang;

public class Token {

    public final TokenType type;
    public final String text;
    public final int pos;
    public int column;
    public int row;

    public Token(TokenType type, String text, int pos, int row, int column) {
        this.type = type;
        this.text = text;
        this.pos = pos;
        this.column = column;
        this.row = row;
    }

    @Override
    public String toString() {
        return text;
    }
}
