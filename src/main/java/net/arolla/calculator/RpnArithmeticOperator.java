package net.arolla.calculator;

import net.arolla.calculator.RpnConverter.RpnLongConverter;

import java.util.ArrayList;
import java.util.List;

import static net.arolla.calculator.RpnEvaluationContext.builder;

final class RpnArithmeticOperator {

    private final RpnEvaluationContext context;
    private final RpnArithmeticOperatorImpl impl;
    private final Integer position;

    private RpnArithmeticOperator(RpnEvaluationContext context, RpnArithmeticOperatorImpl impl, Integer position) {
        this.context = context;
        this.impl = impl;
        this.position = position;
    }

    private enum RpnArithmeticOperatorImpl implements RpnOperator<String> {

        ADDITION("+", 2) {
            @Override
            public String operate(RpnEvaluationContext context, List<String> args) throws InvalidRpnSyntaxException {
                RpnLongConverter converter = context.getRpnConverter();
                return converter.reverseConvert(converter.convert(args.get(0)) + converter.convert(args.get(1)));
            }
        },
        SUBSTRACTION("-", 2) {
            @Override
            public String operate(RpnEvaluationContext context, List<String> args) throws InvalidRpnSyntaxException {
                RpnLongConverter converter = context.getRpnConverter();
                return converter.reverseConvert(converter.convert(args.get(0)) - converter.convert(args.get(1)));
            }
        },
        MULTIPLICATION("*", 2) {
            @Override
            public String operate(RpnEvaluationContext context, List<String> args) throws InvalidRpnSyntaxException {
                RpnLongConverter converter = context.getRpnConverter();
                return converter.reverseConvert(converter.convert(args.get(0)) * converter.convert(args.get(1)));
            }
        },
        DIVISION("/", 2) {
            @Override
            public String operate(RpnEvaluationContext context, List<String> args) throws InvalidRpnSyntaxException {
                RpnLongConverter converter = context.getRpnConverter();
                return converter.reverseConvert(converter.convert(args.get(0)) / converter.convert(args.get(1)));
            }
        },
        INCREMENT("++", 1) {
            @Override
            public String operate(RpnEvaluationContext context, List<String> args) throws InvalidRpnSyntaxException {
                RpnLongConverter converter = context.getRpnConverter();
                return converter.reverseConvert(converter.convert(args.get(0)) + 1);
            }
        },
        DECREMENT("--", 1) {
            @Override
            public String operate(RpnEvaluationContext context, List<String> args) throws InvalidRpnSyntaxException {
                RpnLongConverter converter = context.getRpnConverter();
                return converter.reverseConvert(converter.convert(args.get(0)) - 1);
            }
        },
        MODULO("%", 2) {
            @Override
            public String operate(RpnEvaluationContext context, List<String> args) throws InvalidRpnSyntaxException {
                RpnLongConverter converter = context.getRpnConverter();
                return converter.reverseConvert(converter.convert(args.get(0)) % converter.convert(args.get(1)));
            }
        };

        private final String symbol;
        private final Integer arity;

        RpnArithmeticOperatorImpl(String symbol, Integer arity) {
            this.symbol = symbol;
            this.arity = arity;
        }
    }

    private List<String> evaluate(List<String> tokens) throws InvalidRpnSyntaxException {
        if (position != -1 && position < impl.arity) {
            throw new InvalidRpnSyntaxException(String.format("Insufficient argument count, expected %d, actual was %d", impl.arity, position));
        }
        List<String> newTokens = new ArrayList<>();
        if (position > impl.arity) {
            newTokens.addAll(tokens.subList(0, position - impl.arity));
        }

        newTokens.add(impl.operate(context, tokens.subList(position - impl.arity, position)));
        if (position < tokens.size() - 1) {
            newTokens.addAll(tokens.subList(position + 1, tokens.size()));
        }
        return newTokens;
    }

    private static RpnArithmeticOperator matchFirst(RpnLongConverter converter, List<String> tokens, Integer position) {
        for (RpnArithmeticOperatorImpl operator : RpnArithmeticOperatorImpl.values()) {
            if (operator.symbol.equals(tokens.get(0))) {
                return new RpnArithmeticOperator(builder(converter).build(), operator, position);
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
        RpnArithmeticOperator operator = matchFirst(converter, tokens, 0);
        if (operator == null) {
            return tokens;
        }
        return evaluate(converter, operator.evaluate(tokens));
    }
}
