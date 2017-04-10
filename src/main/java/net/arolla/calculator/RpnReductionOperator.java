package net.arolla.calculator;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class RpnReductionOperator {

    private final RpnConverter converter;
    private final RpnOperator operator;
    private final Matcher matcher;
    private final Integer position;

    private RpnReductionOperator(RpnConverter converter, RpnOperator operator, Matcher matcher, int position) {
        this.converter = converter;
        this.operator = operator;
        this.matcher = matcher;
        this.position = position;
    }

    private interface Operate {
        List<String> operate(RpnConverter converter, Matcher matcher, List<String> items) throws InvalidRpnSyntaxException;
    }

    private enum RpnOperator implements Operate {

        FILTER(Pattern.compile(RpnOperatorConstants.FILTER_REGULAR_EXPRESSION)) {
            @Override
            public List<String> operate(RpnConverter converter, Matcher matcher, List<String> items) throws InvalidRpnSyntaxException {
                RpnComparisonOperator comparator = new RpnComparisonOperator(converter, matcher.group(6));
                RpnExpressionEvaluator evaluator = new RpnExpressionEvaluator(converter);
                List<String> filtered = new ArrayList<>();
                for (String item : items) {
                    String result = evaluator.compute(item, matcher.group(1), matcher.group(3));
                    if (comparator.compare(result, matcher.group(4))) {
                        filtered.add(item);
                    }
                }
                return filtered;
            }
        },

        MAP(Pattern.compile(RpnOperatorConstants.MAP_REGULAR_EXPRESSION)) {
            @Override
            public List<String> operate(RpnConverter converter, Matcher matcher, List<String> items) throws InvalidRpnSyntaxException {
                RpnExpressionEvaluator evaluator = new RpnExpressionEvaluator(converter);
                List<String> mapped = new ArrayList<>();
                for (String item : items) {
                    mapped.add(evaluator.compute(item, matcher.group(1), matcher.group(3)));
                }
                return mapped;
            }
        };

        private final Pattern pattern;

        RpnOperator(Pattern pattern) {
            this.pattern = pattern;
        }

        Matcher matcher(String s) {
            return pattern.matcher(s);
        }

        private static class RpnOperatorConstants {
            private static final String FILTER_REGULAR_EXPRESSION = "^[F]([+-]?(\\d*\\.)?\\d+)([+-/*%])([+-]?(\\d*\\.)?\\d+)(=|<=|>=|>|<|!=)$";
            private static final String MAP_REGULAR_EXPRESSION = "^[M]([+-]?(\\d*\\.)?\\d+)([+-/*%])$";
        }
    }

    private List<String> evaluate(List<String> tokens) throws InvalidRpnSyntaxException {
        List<String> newTokens = operator.operate(converter, matcher, tokens.subList(0, position));
        if (position == tokens.size() - 1) {
            return newTokens;
        }
        newTokens.addAll(tokens.subList(position + 1, tokens.size()));
        return evaluate(converter, newTokens);
    }

    private static RpnReductionOperator matchFirst(RpnConverter converter, List<String> tokens, int position) {
        for (RpnOperator operator : RpnOperator.values()) {
            Matcher matcher = operator.matcher(tokens.get(0));
            if (matcher.find()) {
                return new RpnReductionOperator(converter, operator, matcher, position);
            }
        }
        if (tokens.size() == 1) {
            return null;
        }
        return matchFirst(converter, tokens.subList(1, tokens.size()), ++position);
    }

    static List<String> evaluate(RpnConverter converter, List<String> tokens) throws InvalidRpnSyntaxException {
        if (tokens.isEmpty()) {
            return tokens;
        }
        RpnReductionOperator operator = matchFirst(converter, tokens, 0);
        if (operator == null) {
            return tokens;
        }
        return operator.evaluate(tokens);
    }
}
