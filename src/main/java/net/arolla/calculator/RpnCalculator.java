package net.arolla.calculator;


import static com.google.common.base.Preconditions.checkNotNull;

public class RpnCalculator {

    private final RpnExpressionParser parser = new RpnExpressionParser();

    public String reduce(String expression) throws InvalidRpnSyntaxException {
        checkNotNull(expression);
        return parser.reduce(expression);
    }

    public String filter(String expression) throws InvalidRpnSyntaxException {
        checkNotNull(expression);
        return parser.filter(expression);
    }

}
