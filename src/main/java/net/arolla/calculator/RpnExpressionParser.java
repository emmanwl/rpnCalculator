package net.arolla.calculator;

import java.util.ArrayList;
import java.util.List;

public class RpnExpressionParser {

    private final RpnOperator[] operators;

    public RpnExpressionParser(RpnOperator... operators) {
        this.operators = operators;
    }

    private RpnOperator matchFirst(List<String> tokens){
        if (tokens.isEmpty()) {
            return null;
        }
        for (RpnOperator operator : operators) {
            if (operator.getSymbol().equals(tokens.get(0))) {
                return operator;
            }
        }
        return matchFirst(tokens.subList(1, tokens.size()));
    }

    private List<String> reduceWithOperator(List<String> tokens, RpnOperator operator) throws InvalidRpnSyntaxException {
        Integer index = tokens.indexOf(operator.getSymbol());
        if (index < operator.getArity()) {
            throw new InvalidRpnSyntaxException(String.format("Insufficient argument count, expected %d, actual was %d", operator.getArity(), index));
        }
        List<String> newTokens = new ArrayList<>();
        if (index > operator.getArity()) {
            newTokens.addAll(tokens.subList(0, index - operator.getArity()));
        }
        newTokens.add(operator.operate(tokens.subList(index - operator.getArity(), index).toArray(new String[operator.getArity()])));
        if (index < tokens.size() - 1) {
            newTokens.addAll(tokens.subList(index + 1, tokens.size()));
        }
        return reduce(newTokens);
    }

    public List<String> reduce(List<String> tokens) throws InvalidRpnSyntaxException {
        if (tokens.size() <= 1) {
            if (tokens.isEmpty()) {
                throw new InvalidRpnSyntaxException("Empty rpn expression");
            }
            return tokens;
        }
        RpnOperator operator = matchFirst(tokens);
        if (operator == null) {
            if (tokens.size() != 1) {
                throw new InvalidRpnSyntaxException("Invalid reduced rpn expression");
            }
            return tokens;
        }
        return reduceWithOperator(tokens, operator);
    }

}
