package net.arolla.calculator;


import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkState;
import static java.lang.String.valueOf;
import static java.util.Arrays.asList;

public class RpnCalculator {

    private static final List<String> OPERATORS = ImmutableList.of("+", "-", "*", "/");
    private static final String RPN_TOKEN_SEPARATOR = " ";
    private static final String VALID_REDUCED_RPN_EXPRESSION_REGEXP = "^[-|+]?\\d+(\\s[-|+]?\\d+)*$";

    public String evaluate(String expression) throws InvalidRpnSyntaxException {
        checkState(expression != null);
        return evaluate(asList(expression.split(RPN_TOKEN_SEPARATOR)));
    }

    private int firstIndexOfAnyOperator(List<String> tokens) {
        int min = tokens.indexOf(OPERATORS.get(0));
        for (int i = 1; i < OPERATORS.size(); i++) {
            int index = tokens.indexOf(OPERATORS.get(i));
            if (index != -1 && (index < min || min == -1)) {
                min = index;
            }
        }
        return min;
    }

    private List<String> reduce(List<String> tokens) throws InvalidRpnSyntaxException {
        if (tokens.size() == 1) {
            return tokens;
        }
        int index = firstIndexOfAnyOperator(tokens);
        if (index == -1) {
            return tokens;
        } else if (index < 2) {
            throw new InvalidRpnSyntaxException("Invalid RPN expression: [" + tokens.get(0) + " " + tokens.get(1) + "]");
        }
        List<String> newList = new ArrayList<>();
        if (index >= 3) {
            newList.addAll(tokens.subList(0, index - 2));
        }
        newList.add(computeSimple(tokens.get(index - 2), tokens.get(index), tokens.get(index - 1)));
        if (index < tokens.size() - 1) {
            newList.addAll(tokens.subList(index + 1, tokens.size()));
        }
        return reduce(newList);
    }

    private String evaluate(List<String> tokens) throws InvalidRpnSyntaxException {
        List<String> reduced = reduce(new ArrayList<>(tokens));
        String joined = Joiner.on(" ").join(reduced);
        if (!joined.matches(VALID_REDUCED_RPN_EXPRESSION_REGEXP)) {
            throw new InvalidRpnSyntaxException("Invalid RPN expression: " + joined);
        }
        return joined;
    }

    private Integer toInteger(String number) throws InvalidRpnSyntaxException {
        try {
            return Integer.valueOf(number);
        } catch (NumberFormatException e) {
            throw new InvalidRpnSyntaxException(e.getMessage());
        }
    }

    private String computeSimple(String leftOperand, String operator, String rightOperand) throws InvalidRpnSyntaxException {
        Integer left = toInteger(leftOperand);
        Integer right = toInteger(rightOperand);
        switch (operator) {
            case "/" : return valueOf(left / right);
            case "-" : return valueOf(left - right);
            case "*" : return valueOf(left * right);
            case "+" : return valueOf(left + right);
            default: throw new InvalidRpnSyntaxException("Unknown operator " + operator);
        }
    }
}
