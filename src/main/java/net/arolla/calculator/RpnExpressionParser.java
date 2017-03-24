package net.arolla.calculator;

import com.google.common.base.Joiner;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

public class RpnExpressionParser {

    private static final String RPN_TOKEN_SEPARATOR = " ";

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
        List<String> newTokens = operator.filter(index, tokens);
        if (index == tokens.size() - 1) {
            return newTokens;
        }
        newTokens.addAll(tokens.subList(index + 1, tokens.size()));
        return filter(newTokens);
    }

    private List<String> reduce(List<String> tokens) throws InvalidRpnSyntaxException {
        if (tokens.size() <= 1) {
            return tokens;
        }
        RpnFixedArityOperator operator = RpnFixedArityOperator.matchFirst(tokens);
        if (operator == null) {
            return tokens;
        }
        return reduceWithOperator(tokens, operator);
    }

    private List<String> filter(List<String> tokens) throws InvalidRpnSyntaxException {
        if (tokens.size() <= 1) {
            return tokens;
        }
        RpnArbitraryArityOperator operator = RpnArbitraryArityOperator.matchFirst(tokens);
        if (operator == null) {
            return tokens;
        }
        return filterWithOperator(tokens, operator);
    }

    private List<String> split(String expression) {
        return asList(expression.split(RPN_TOKEN_SEPARATOR));
    }

    private String join(List<String> tokens) {
        return Joiner.on(RPN_TOKEN_SEPARATOR).join(tokens);
    }

    public String reduce(String expression) throws InvalidRpnSyntaxException {
        List<String> tokens = split(expression);
        return join(reduce(tokens));
    }

    public String filter(String expression) throws InvalidRpnSyntaxException {
        List<String> tokens = split(expression);
        return join(filter(tokens));
    }
}
