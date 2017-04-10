package net.arolla.calculator;

import com.google.common.base.Joiner;

import java.util.List;

import static java.util.Arrays.asList;

public final class RpnExpressionEvaluator {

    private static final String RPN_TOKEN_SEPARATOR_REGEXP = "\\s+";
    private static final String RPN_TOKEN_SEPARATOR = " ";

    private final RpnConverter converter;

    public RpnExpressionEvaluator(RpnConverter converter) {
        this.converter = converter;
    }

    private static String[] split(String expression) {
        return expression.split(RPN_TOKEN_SEPARATOR_REGEXP);
    }

    private static String join(List<String> tokens) {
        return Joiner.on(RPN_TOKEN_SEPARATOR).join(tokens);
    }

    public String compute(String expression) throws InvalidRpnSyntaxException {
        return compute(split(expression));
    }

    public String compute(String... tokens) throws InvalidRpnSyntaxException {
        return join(RpnArithmeticOperator.evaluate(converter, asList(tokens)));
    }

    public String reduce(String expression) throws InvalidRpnSyntaxException {
        return reduce(split(expression));
    }

    public String reduce(String... tokens) throws InvalidRpnSyntaxException {
        return join(RpnReductionOperator.evaluate(converter, asList(tokens)));
    }
}