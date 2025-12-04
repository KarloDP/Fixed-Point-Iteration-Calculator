package org.group.calculator.core.evaluator;

public interface Evaluator {
    double evaluate(double x);
    boolean isValid();
    String getExpression();
}