package net.arolla.calculator;

import com.google.common.base.Joiner;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

public class RpnExpressionParser {

    private static final String RPN_TOKEN_SEPARATOR = " ";
    private final String INTEGER = "^[+|-]?\\d+$";

    private List<String> reduceWithOperator(List<String> tokens, RpnFixedArityOperator operator) throws InvalidRpnSyntaxException {
        Integer index = operator.getSymbolIndex(tokens);
        List<String> newTokens = new ArrayList<>();
        if (index > operator.getArity()) {
            newTokens.addAll(tokens.subList(0, index - operator.getArity()));
        }
        newTokens.add(operator.reduce(tokens.subList(index - operator.getArity(), index).toArray(new String[operator.getArity()])));
        if (index < tokens.size() - 1) {
            newTokens.addAll(tokens.subList(index + 1, tokens.size()));
        }
        return reduce(newTokens);
    }

    private List<String> filterWithOperator(List<String> tokens, RpnArbitraryArityOperator operator) throws InvalidRpnSyntaxException {
        Integer index = operator.getSymbolIndex(tokens);
        List<String> newTokens = new ArrayList<>(operator.filter(index, tokens));
        if (index >= tokens.size() - 1) {
            return newTokens;
        }
        newTokens.addAll(tokens.subList(index + 1, tokens.size()));
        return filter(newTokens);
    }

    private void checkOperand(String argument) throws InvalidRpnSyntaxException {
        if (!argument.matches(INTEGER)) {
            throw new InvalidRpnSyntaxException("Not a valid rpn operand: " + argument);
        }
    }

    private List<String> reduce(List<String> tokens) throws InvalidRpnSyntaxException {
        if (tokens.size() == 1) {
            checkOperand(tokens.get(0));
            return tokens;
        }
        RpnFixedArityOperator operator = RpnFixedArityOperator.matchFirst(tokens);
        if (operator == null) {
            for (String token : tokens) {
                checkOperand(token);
            }
            return tokens;
        }
        return reduceWithOperator(tokens, operator);
    }

    private void checkOperator(String argument) throws InvalidRpnSyntaxException {
        if (!RpnArbitraryArityOperator.FILTER.matches(argument)) {
            throw new InvalidRpnSyntaxException("Not a valid rpn operator: " + argument);
        }
    }

    private List<String> filter(List<String> tokens) throws InvalidRpnSyntaxException {
        if (tokens.isEmpty()) {
            return tokens;
        }
        if (tokens.size() == 1) {
            checkOperand(tokens.get(0));
            return tokens;
        }
        RpnArbitraryArityOperator operator = RpnArbitraryArityOperator.matchFirst(tokens);
        if (operator == null) {
            for (String token : tokens) {
                checkOperand(token);
            }
            return tokens;
        }
        checkOperator(tokens.get(tokens.size() - 1));
        return filterWithOperator(tokens, operator);
    }

    private void checkEmpty(List<String> tokens) throws InvalidRpnSyntaxException {
        if (tokens.isEmpty()) {
            throw new InvalidRpnSyntaxException("Empty rpn expression");
        }
    }

    public String reduce(String expression) throws InvalidRpnSyntaxException {
        List<String> tokens = asList(expression.split(RPN_TOKEN_SEPARATOR));
        checkEmpty(tokens);
        return Joiner.on(RPN_TOKEN_SEPARATOR).join(reduce(tokens));
    }

    public String filter(String expression) throws InvalidRpnSyntaxException {
        List<String> tokens = asList(expression.split(RPN_TOKEN_SEPARATOR));
        checkEmpty(tokens);
        return Joiner.on(RPN_TOKEN_SEPARATOR).join(filter(tokens));
    }
}
