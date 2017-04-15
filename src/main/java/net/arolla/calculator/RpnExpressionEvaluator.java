package net.arolla.calculator;

import com.google.common.base.Joiner;
import net.arolla.calculator.RpnConverter.RpnLongConverter;

import java.util.List;

import static java.util.Arrays.asList;

final class RpnExpressionEvaluator {

    private static final String RPN_TOKEN_SEPARATOR_REGEXP = "\\s+";
    private static final String RPN_TOKEN_SEPARATOR = " ";

    private final RpnLongConverter converter;

    RpnExpressionEvaluator(RpnLongConverter converter) {
        this.converter = converter;
    }

    private static String[] split(String expression) {
        return expression.split(RPN_TOKEN_SEPARATOR_REGEXP);
    }

    private static String join(List<String> tokens) {
        return Joiner.on(RPN_TOKEN_SEPARATOR).join(tokens);
    }

    String compute(String expression) throws InvalidRpnSyntaxException {
        return compute(split(expression));
    }

    String compute(String... tokens) throws InvalidRpnSyntaxException {
        return join(RpnArithmeticOperator.evaluate(converter, asList(tokens)));
    }

    String reduce(String expression) throws InvalidRpnSyntaxException {
        return join(RpnReductionOperator.evaluate(converter, asList(split(expression))));
    }

}