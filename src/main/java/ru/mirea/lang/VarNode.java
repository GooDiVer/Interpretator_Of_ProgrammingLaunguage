package ru.mirea.lang;

public class VarNode extends ExprNode {

    final Token id;

    public VarNode(Token id) {
        this.id = id;
    }
}
