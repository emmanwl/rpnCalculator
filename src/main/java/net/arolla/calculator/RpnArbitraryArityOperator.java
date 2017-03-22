package net.arolla.calculator;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.checkArgument;

public enum RpnArbitraryArityOperator implements Filter<String> {

    FILTER("F", -1) {
        @Override
        public List<String> filter(int index, List<String> items) throws InvalidRpnSyntaxException {
            Matcher matcher = FILTER_PATTERN.matcher(items.get(index));
            List<String> filtered = new ArrayList<>();
            if (matcher.find()) {
                List<Integer> indices = new ArrayList<>();
                List<String> results = computeIntermediaryResults(matcher.group(2), items.subList(0, index), matcher.group(1));
                for (int i = 0; i < results.size(); i++) {
                    if (RpnArbitraryArityOperator.isConditionSatisfied(results.get(i), matcher.group(4), matcher.group(3))) {
                        indices.add(i);
                    }
                }
                for (int i = 0; i < indices.size(); i++) {
                    filtered.add(items.get(indices.get(i)));
                }
            }
            return filtered;
        }

        @Override
        public boolean matches(String token) {
            return FILTER_PATTERN.matcher(token).matches();
        }
    };

    private static final String FILTER_EXPRESSION_REGEXP = "^[F]([+|-]?\\d)([+|-|/|*|%])([+|-]?\\d)(=|<=|>=|>|<|!=)$";
    private static final Pattern FILTER_PATTERN = Pattern.compile(FILTER_EXPRESSION_REGEXP);
    private final String symbol;
    private final Integer arity;

    RpnArbitraryArityOperator(String symbol, Integer arity) {
        checkArgument(symbol != null && !symbol.isEmpty(), "Operator symbol cannot be empty");
        this.symbol = symbol;
        this.arity = arity;
    }

    private static Long longValue(String number) throws InvalidRpnSyntaxException {
        try {
            return Long.valueOf(number);
        } catch (NumberFormatException e) {
            throw new InvalidRpnSyntaxException(e.getMessage());
        }
    }

    private static List<String> computeIntermediaryResults(String operator, List<String> items, String operand) throws InvalidRpnSyntaxException {
        switch(operator) {
            case "+" :
                return RpnFixedArityOperator.ADDITION.apply(operand, items);
            case "-":
                return RpnFixedArityOperator.SUBSTRACTION.apply(operand, items);
            case "*":
                return RpnFixedArityOperator.MULTIPLICATION.apply(operand, items);
            case "/":
                return RpnFixedArityOperator.DIVISION.apply(operand, items);
            case "%":
                return RpnFixedArityOperator.MODULO.apply(operand, items);
            default:
                throw new InvalidRpnSyntaxException("Unknown expression operator: " + operator);
        }
    }

    private static boolean isConditionSatisfied(String left, String operator, String right) throws InvalidRpnSyntaxException {
        switch(operator) {
            case "=" :
                return longValue(left) == longValue(right);
            case "!=":
                return longValue(left) != longValue(right);
            case "<":
                return longValue(left) < longValue(right);
            case "<=":
                return longValue(left) <= longValue(right);
            case ">":
                return longValue(left) > longValue(right);
            case ">=":
                return longValue(left) >= longValue(right);
            default:
                throw new InvalidRpnSyntaxException("Unknown condition operator: " + operator);
        }
    }

    public static RpnArbitraryArityOperator matchFirst(List<String> tokens) {
        if (tokens.isEmpty()) {
            return null;
        }
        for (RpnArbitraryArityOperator operator : values()) {
            if (operator.matches(tokens.get(0))) {
                return operator;
            }
        }
        return matchFirst(tokens.subList(1, tokens.size()));
    }

    public Integer getSymbolIndex(List<String> tokens) throws InvalidRpnSyntaxException {
        for (int i = 0; i < tokens.size(); i++) {
            if (this.matches(tokens.get(i))) {
                return i;
            }
        }
        return -1;
    }
}
