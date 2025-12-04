package org.group.calculator.core.evaluator;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class Exp4jEvaluator implements Evaluator {

    private final String expressionString;
    private final Expression expression;

    public Exp4jEvaluator(String expressionString) {
        this.expressionString = expressionString;
        Expression temp;
        try {
            temp = new ExpressionBuilder(expressionString)
                    .variables("x")
                    .build();
        } catch (Exception e) {
            temp = null;
        }
        this.expression = temp;
    }

    @Override
    public double evaluate(double x) {
        if (expression == null) return Double.NaN;
        try {
            return expression.setVariable("x", x).evaluate();
        } catch (Exception e) {
            return Double.NaN;
        }
    }

    @Override
    public boolean isValid() {
        return expression != null;
    }

    @Override
    public String getExpression() {
        return expressionString;
    }
}
