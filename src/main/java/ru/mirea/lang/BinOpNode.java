package ru.mirea.lang;

public class BinOpNode extends ExprNode {

    final Token op;
    final ExprNode left;
    final ExprNode right;

    public BinOpNode(Token op, ExprNode left, ExprNode right) {
        this.op = op;
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        return "(" + left.toString() + op.text + right.toString() + ")";
    }
}
