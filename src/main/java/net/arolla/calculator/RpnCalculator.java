package net.arolla.calculator;


import static com.google.common.base.Preconditions.checkNotNull;

public class RpnCalculator {

    private final RpnExpressionEvaluator evaluator = new RpnExpressionEvaluator();

    public String reduce(String expression) throws InvalidRpnSyntaxException {
        checkNotNull(expression);
        return evaluator.reduce(expression);
    }

    public String filter(String expression) throws InvalidRpnSyntaxException {
        checkNotNull(expression);
        return evaluator.filter(expression);
    }

}
