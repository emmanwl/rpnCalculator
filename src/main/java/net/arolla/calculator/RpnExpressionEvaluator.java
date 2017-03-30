package net.arolla.calculator;

import com.google.common.base.Joiner;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static net.arolla.calculator.RpnReductionOperator.evaluate;

public class RpnExpressionEvaluator {

    private static final String RPN_TOKEN_SEPARATOR = " ";

    private List<String> split(String expression) {
        return asList(expression.split(RPN_TOKEN_SEPARATOR));
    }

    private String join(List<String> tokens) {
        return Joiner.on(RPN_TOKEN_SEPARATOR).join(tokens);
    }

    public String reduce(String expression) throws InvalidRpnSyntaxException {
        List<String> tokens = split(expression);
        return join(evaluate(tokens));
    }

    public String filter(String expression) throws InvalidRpnSyntaxException {
        List<String> tokens = split(expression);
        return join(RpnFilteringOperator.evaluate(tokens));
    }
}
