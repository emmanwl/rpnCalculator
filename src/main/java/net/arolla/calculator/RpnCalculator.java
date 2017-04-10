package net.arolla.calculator;


import static com.google.common.base.Preconditions.checkNotNull;

public final class RpnCalculator {

    private final RpnExpressionEvaluator evaluator = new RpnExpressionEvaluator(new RpnConverter());

    public String compute(String expression) throws InvalidRpnSyntaxException {
        checkNotNull(expression);
        return evaluator.compute(expression);
    }

    public String reduce(String expression) throws InvalidRpnSyntaxException {
        checkNotNull(expression);
        return evaluator.reduce(expression);
    }
}
