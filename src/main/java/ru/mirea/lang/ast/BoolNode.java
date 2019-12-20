package ru.mirea.lang.ast;

import ru.mirea.lang.Token;

public class BoolNode extends ExprNode {

    public final Token value;

    public BoolNode(Token value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value.text;
    }
}
