package net.arolla.calculator;


import com.google.common.collect.ImmutableList;

import java.util.Arrays;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.valueOf;

public class RpnCalculator {

    private static final List<String> OPERATORS = ImmutableList.of("+", "-", "*", "/");
    private static final String RPN_TOKEN_SEPARATOR = " ";

    public String evaluate(String expression) {
        checkArgument(expression != null && expression.length() >=3, "Invalid rpn expression: " + expression);
        return evaluate(Arrays.asList(expression.split(RPN_TOKEN_SEPARATOR)));
    }

    private String evaluate(List<String> tokens) {
        Integer index = tokens.size() - 1;
        String operator = tokens.get(index);
        checkArgument(isOperator(operator), "Unknown operator " + operator);
        List<String> allExceptLast = tokens.subList(0, index);
        return computeSimple(
                evaluateLeftOperand(allExceptLast),
                operator,
                evaluateRightOperandRecursively(allExceptLast));
    }

    private String evaluateLeftOperand(List<String> tokens) {
        if (tokens.size() == 2) {
            return tokens.get(0);
        }
        if (isOperator(tokens.get(2))) {
            return computeSimple(tokens.get(0), tokens.get(2), tokens.get(1));
        }
        return tokens.get(0);
    }

    private String evaluateRightOperandRecursively(List<String> tokens) {
        if (tokens.size() == 2) {
            return tokens.get(1);
        }
        if (isOperator(tokens.get(2))) {
            return evaluate(tokens.subList(3, tokens.size()));
        }
        return evaluate(tokens.subList(1, tokens.size()));
    }

    private boolean isOperator(String operator) {
        return OPERATORS.contains(operator);
    }

    private String computeSimple(String leftOperand, String operator, String rightOperand) {
        Integer left = Integer.valueOf(leftOperand);
        Integer right = Integer.valueOf(rightOperand);
        switch (operator) {
            case "/" : return valueOf(left / right);
            case "-" : return valueOf(left - right);
            case "*" : return valueOf(left * right);
            case "+" : return valueOf(left + right);
            default: return "Undefined";
        }
    }
}
