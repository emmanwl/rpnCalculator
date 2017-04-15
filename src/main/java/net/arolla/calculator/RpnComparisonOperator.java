package net.arolla.calculator;

import net.arolla.calculator.RpnConverter.RpnLongConverter;

import java.util.List;

import static net.arolla.calculator.RpnEvaluationContext.*;

final class RpnComparisonOperator {

    private final RpnEvaluationContext context;
    private final RpnComparisonOperatorImpl impl;

    private RpnComparisonOperator(RpnEvaluationContext context, RpnComparisonOperatorImpl impl) {
        this.context = context;
        this.impl = impl;
    }

    private enum RpnComparisonOperatorImpl implements RpnOperator<Boolean> {

        EQUAL("=") {
            @Override
            public Boolean operate(RpnEvaluationContext context, List<String> args) throws InvalidRpnSyntaxException {
                RpnLongConverter converter = context.getRpnConverter();
                return converter.convert(args.get(0)).compareTo(converter.convert(args.get(1))) == 0;
            }
        },
        LOWER("<") {
            @Override
            public Boolean operate(RpnEvaluationContext context, List<String> args) throws InvalidRpnSyntaxException {
                RpnLongConverter converter = context.getRpnConverter();
                return converter.convert(args.get(0)).compareTo(converter.convert(args.get(1))) < 0;
            }
        },
        GREATER(">") {
            @Override
            public Boolean operate(RpnEvaluationContext context, List<String> args) throws InvalidRpnSyntaxException {
                RpnLongConverter converter = context.getRpnConverter();
                return converter.convert(args.get(0)).compareTo(converter.convert(args.get(1))) > 0;
            }
        },
        DIFFERENT("!=") {
            @Override
            public Boolean operate(RpnEvaluationContext context, List<String> args) throws InvalidRpnSyntaxException {
                RpnLongConverter converter = context.getRpnConverter();
                return converter.convert(args.get(0)).compareTo(converter.convert(args.get(1))) != 0;
            }
        },
        LOWER_THAN_OR_EQUAL("<=") {
            @Override
            public Boolean operate(RpnEvaluationContext context, List<String> args) throws InvalidRpnSyntaxException {
                RpnLongConverter converter = context.getRpnConverter();
                return converter.convert(args.get(0)).compareTo(converter.convert(args.get(1))) <= 0;
            }
        },
        GREATER_THAN_OR_EQUAL(">=") {
            @Override
            public Boolean operate(RpnEvaluationContext context, List<String> args) throws InvalidRpnSyntaxException {
                RpnLongConverter converter = context.getRpnConverter();
                return converter.convert(args.get(0)).compareTo(converter.convert(args.get(1))) >= 0;
            }
        };

        private final String symbol;
        private final Integer arity;

        RpnComparisonOperatorImpl(String symbol) {
            this.symbol = symbol;
            this.arity = 2;
        }
    }

    private Boolean evaluate(List<String> tokens) throws InvalidRpnSyntaxException {
        if (impl.arity > tokens.size()) {
            throw new InvalidRpnSyntaxException(String.format("Insufficient argument count, expected %d, actual was %d", impl.arity, tokens.size()));
        }
        return impl.operate(context, tokens);
    }

    private static RpnComparisonOperator matchFirst(RpnLongConverter converter, String token) {
        for (RpnComparisonOperatorImpl operator : RpnComparisonOperatorImpl.values()) {
            if (operator.symbol.equals(token)) {
                return new RpnComparisonOperator(builder(converter).build(), operator);
            }
        }
        return null;
    }

    static Boolean evaluate(RpnLongConverter converter, List<String> tokens) throws InvalidRpnSyntaxException {
        if (tokens.isEmpty()) {
            throw new InvalidRpnSyntaxException("Could not evaluate empty expression");
        }
        RpnComparisonOperator operator = matchFirst(converter, tokens.get(tokens.size() - 1));
        if (operator == null) {
            throw new InvalidRpnSyntaxException("Could not map comparison symbol " + tokens.get(tokens.size() - 1));
        }

        return operator.evaluate(tokens.subList(0, tokens.size() - 1));
    }
}
