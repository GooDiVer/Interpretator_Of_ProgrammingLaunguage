package ru.mirea.lang;

import ru.mirea.lang.ast.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Parser {

    private final List<Token> tokens;
    private int pos = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    private void error(String message) {
        if (pos < tokens.size()) {
            Token t = tokens.get(pos);
            throw new RuntimeException(message + " в позиции " + t.row + ":" + t.column);
        } else {
            throw new RuntimeException(message + " в конце файла");
        }
    }

    private Token match(TokenType... expected) {
        if (pos < tokens.size()) {
            Token curr = tokens.get(pos);
            if (Arrays.asList(expected).contains(curr.type)) {
                pos++;
                return curr;
            }
        }
        return null;
    }

    private Token require(TokenType... expected) {
        Token t = match(expected);
        if (t == null)
            error("Ожидается " + Arrays.toString(expected));
        return t;
    }

    // true and (x or  not y ) or false xor x
    // x or y

    public List<StatementNode> parse() {
        List<StatementNode> list = new ArrayList<>();
        while (pos < tokens.size()) {
            list.add(parseStatement());
        }

        return list;
    }

    public StatementNode parseStatement() {
        Token op = require(TokenType.IDENT, TokenType.PRINT);
        switch (op.type) {
            case IDENT:
                Token token = require(TokenType.ASSIGN);
                ExprNode e = parseExpression();
                require(TokenType.SEMICOLON);
                return new AssignNode(token, e, new VarNode(op));
            case PRINT:
                Token id = require(TokenType.IDENT);
                require(TokenType.SEMICOLON);
                return new PrintNode(op, new VarNode(id));

        }
        return null;
    }

    public ExprNode parseExpression() {
        ExprNode e1 = parseSlag();
        Token op;
        while ((op = match(TokenType.OR, TokenType.XOR)) != null) {
            ExprNode e2 = parseSlag();
            e1 = new BinOpNode(op, e1, e2);
        }
        return e1;
    }

    public ExprNode parseSlag() {
        ExprNode e1 = parseMnozh();
        Token op;
        while ((op = match(TokenType.AND)) != null) {
            ExprNode e2 = parseMnozh();
            e1 = new BinOpNode(op, e1, e2);
        }
        return e1;
    }

    private ExprNode parseMnozh() {
        if (match(TokenType.LPAR) != null) {
            ExprNode e = parseExpression();
            require(TokenType.RPAR);
            return e;
        } else {
            return parseElem();
        }
    }

    private ExprNode parseElem() {

        Token ident = match(TokenType.IDENT);
        if (ident != null)
            return new VarNode(ident);

        Token tr = match(TokenType.TRUE);
        if (tr != null)
            return new BoolNode(tr);

        Token fal = match(TokenType.FALSE);
        if (fal != null)
            return new BoolNode(fal);

        Token no = match(TokenType.NOT);
        if (no != null)
            return new UnOpNode(no, parseMnozh());


        error("Ожидается число или переменная");
        return null;
    }

//    public static int eval(ExprNode node) {
//        if (node instanceof NumberNode) {
//            NumberNode num = (NumberNode) node;
//            return Integer.parseInt(num.number.text);
//        } else if (node instanceof BinOpNode) {
//            BinOpNode binOp = (BinOpNode) node;
//            int l = eval(binOp.left);
//            int r = eval(binOp.right);
//            switch (binOp.op.type) {
//                case ADD: return l + r;
//                case SUB: return l - r;
//                case MUL: return l * r;
//                case DIV: return l / r;
//            }
//        } else if (node instanceof VarNode) {
//            VarNode var = (VarNode) node;
//            System.out.println("Введите значение " + var.id.text + ":");
//            String line = new Scanner(System.in).nextLine();
//            return Integer.parseInt(line);
//        }
//        throw new IllegalStateException();
//    }

    public static void main(String[] args) {
        String text = "y := false;\n" +
                "x := true;\n" +
                "y := not y;\n" +
                "print y;\n" +
                "z := (x and y) or not true;\n" +
                "print z;";

        Lexer l = new Lexer(text);
        //Get array with tokens
        List<Token> tokens = l.lex();
        System.out.println();
        //
        tokens.removeIf(t -> t.type == TokenType.SPACE);
        tokens.removeIf(t -> t.type == TokenType.ENDL);

        Parser p = new Parser(tokens);
        List<StatementNode> list = p.parse();

        Interpreter inpr = new Interpreter();
        inpr.evalProgram(list, new HashMap<>());
    }
}
