package net.arolla.calculator;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.checkArgument;

public class RpnFilteringOperator implements Filter<String> {

    private static final String FILTER_EXPRESSION_REGEXP = "^[F]([+|-]?\\d)([+|-|/|*|%])([+|-]?\\d)(=|<=|>=|>|<|!=)$";
    private static final Pattern FILTER_PATTERN = Pattern.compile(FILTER_EXPRESSION_REGEXP);
    private final Matcher matcher;
    private final Integer position;

    private RpnFilteringOperator(String symbol, int position) throws InvalidRpnSyntaxException {
        this.matcher = FILTER_PATTERN.matcher(symbol);
        this.matcher.find();
        this.position = position;
    }

    private static RpnFilteringOperator matchFirst(List<String> tokens, int position) throws InvalidRpnSyntaxException{
        if (tokens.isEmpty()) {
            return null;
        }
        if (FILTER_PATTERN.matcher(tokens.get(0)).find()) {
            return new RpnFilteringOperator(tokens.get(0), position);
        }
        return matchFirst(tokens.subList(1, tokens.size()), ++position);
    }

    private List<String> evaluateWithOperator(List<String> tokens) throws InvalidRpnSyntaxException {
        List<String> newTokens = filter(tokens.subList(0, position));
        if (position == tokens.size() - 1) {
            return newTokens;
        }
        newTokens.addAll(tokens.subList(position + 1, tokens.size()));
        return evaluate(newTokens);
    }

    public static List<String> evaluate(List<String> tokens) throws InvalidRpnSyntaxException {
        if (tokens.size() <= 1) {
            return tokens;
        }
        RpnFilteringOperator operator = matchFirst(tokens, 0);
        if (operator == null) {
            return tokens;
        }
        return operator.evaluateWithOperator(tokens);
    }

    @Override
    public List<String> filter(List<String> items) throws InvalidRpnSyntaxException {
        RpnReductionOperator reducer = new RpnReductionOperator(new RpnArithmeticOperator(matcher.group(2)));
        RpnComparisonOperator comparator = new RpnComparisonOperator(matcher.group(4));
        List<String> filtered = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            if (comparator.compare(reducer.reduce(items.get(i), matcher.group(1)), matcher.group(3))) {
                filtered.add(items.get(i));
            }
        }
        return filtered;
    }

}
