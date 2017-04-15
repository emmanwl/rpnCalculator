package net.arolla.calculator;

import net.arolla.calculator.RpnConverter.RpnLongConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Arrays.asList;
import static net.arolla.calculator.RpnEvaluationContext.builder;

final class RpnReductionOperator {

    private final RpnEvaluationContext context;
    private final RpnReductionOperatorImpl impl;
    private final Integer position;

    private RpnReductionOperator(RpnEvaluationContext context, RpnReductionOperatorImpl impl, int position) {
        this.context = context;
        this.impl = impl;
        this.position = position;
    }

    private enum RpnReductionOperatorImpl implements RpnOperator<List<String>> {

        FILTER(Pattern.compile(RpnOperatorConstants.FILTER_REGULAR_EXPRESSION)) {
            @Override
            public List<String> operate(RpnEvaluationContext context, List<String> args) throws InvalidRpnSyntaxException {
                RpnLongConverter converter = context.getRpnConverter();
                Matcher matcher = context.getMatcher();
                RpnExpressionEvaluator evaluator = new RpnExpressionEvaluator(converter);
                List<String> filtered = new ArrayList<>();
                for (String arg : args) {
                    String result = evaluator.compute(arg, matcher.group(1), matcher.group(3));
                    if (RpnComparisonOperator.evaluate(converter, asList(result, matcher.group(4), matcher.group(6)))) {
                        filtered.add(arg);
                    }
                }
                return filtered;
            }
        },

        MAP(Pattern.compile(RpnOperatorConstants.MAP_REGULAR_EXPRESSION)) {
            @Override
            public List<String> operate(RpnEvaluationContext context, List<String> args) throws InvalidRpnSyntaxException {
                RpnLongConverter converter = context.getRpnConverter();
                Matcher matcher = context.getMatcher();
                RpnExpressionEvaluator evaluator = new RpnExpressionEvaluator(converter);
                List<String> mapped = new ArrayList<>();
                for (String arg : args) {
                    mapped.add(evaluator.compute(arg, matcher.group(1), matcher.group(3)));
                }
                return mapped;
            }
        };

        private final Pattern pattern;

        RpnReductionOperatorImpl(Pattern pattern) {
            this.pattern = pattern;
        }

        private static class RpnOperatorConstants {
            private static final String FILTER_REGULAR_EXPRESSION = "^[F]([+-]?(\\d*\\.)?\\d+)([+-/*%])([+-]?(\\d*\\.)?\\d+)(=|<=|>=|>|<|!=)$";
            private static final String MAP_REGULAR_EXPRESSION = "^[M]([+-]?(\\d*\\.)?\\d+)([+-/*%])$";
        }
    }

    private List<String> evaluate(List<String> tokens) throws InvalidRpnSyntaxException {
        List<String> newTokens = impl.operate(context, tokens.subList(0, position));
        if (position == tokens.size() - 1) {
            return newTokens;
        }
        newTokens.addAll(tokens.subList(position + 1, tokens.size()));
        return newTokens;
    }

    private static RpnReductionOperator matchFirst(RpnLongConverter converter, List<String> tokens, int position) {
        for (RpnReductionOperatorImpl operator : RpnReductionOperatorImpl.values()) {
            Matcher matcher = operator.pattern.matcher(tokens.get(0));
            if (matcher.find()) {
                return new RpnReductionOperator(builder(converter).withMatcher(matcher).build(), operator, position);
            }
        }
        if (tokens.size() == 1) {
            return null;
        }
        return matchFirst(converter, tokens.subList(1, tokens.size()), ++position);
    }

    static List<String> evaluate(RpnLongConverter converter, List<String> tokens) throws InvalidRpnSyntaxException {
        if (tokens.isEmpty()) {
            return tokens;
        }
        RpnReductionOperator operator = matchFirst(converter, tokens, 0);
        if (operator == null) {
            return tokens;
        }
        return evaluate(converter, operator.evaluate(tokens));
    }
}
