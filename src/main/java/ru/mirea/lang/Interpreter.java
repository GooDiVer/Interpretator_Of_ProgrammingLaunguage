package ru.mirea.lang;

import ru.mirea.lang.ast.*;

import java.util.List;
import java.util.Map;

import static ru.mirea.lang.TokenType.AND;
import static ru.mirea.lang.TokenType.OR;

public class Interpreter {

    public void evalProgram(List<StatementNode> program, Map<String, Boolean> vars) {
        for ( StatementNode stmt: program) {
            evalStatement(stmt, vars);
        }
    }

    private void evalStatement( StatementNode stmt, Map<String, Boolean> vars) {
        if (stmt instanceof AssignNode) {
            vars.put(((AssignNode) stmt).varNode.id.text, evalExpression(((AssignNode) stmt).exprNode,vars));
        }
        if (stmt instanceof PrintNode) {
            System.out.println(vars.get(((PrintNode) stmt).varNode.id.text));
        }

    }

    private boolean evalExpression(ExprNode e, Map<String, Boolean> vars) {
        if(e instanceof BoolNode) return Boolean.parseBoolean(((BoolNode) e).value.text);
        if(e instanceof VarNode) return vars.get(((VarNode) e).id.text);
        if(e instanceof UnOpNode) return !evalExpression(((UnOpNode) e).node ,vars);
        if(e instanceof BinOpNode) {
            boolean valueL = evalExpression(((BinOpNode)e).left, vars);
            boolean valueR = evalExpression(((BinOpNode)e).right, vars);

            switch (((BinOpNode) e).op.type) {
                case AND:
                    return valueL && valueR;
                case OR:
                    return valueL || valueR;
                case XOR:
                    return valueL ^ valueR;

            }
        }
        return false;
    }
}
