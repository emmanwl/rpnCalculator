package net.arolla.calculator;

import java.util.ArrayList;
import java.util.List;

final class RpnArithmeticOperator {

    private final RpnConverter converter;
    private final RpnOperator operator;
    private final Integer position;

    private RpnArithmeticOperator(RpnConverter converter, RpnOperator operator, Integer position) throws InvalidRpnSyntaxException {
        this.converter = converter;
        this.operator = operator;
        this.position = position;
    }

    private interface Compute {
        String compute(RpnConverter converter, String... args) throws InvalidRpnSyntaxException;
    }

    private enum RpnOperator implements Compute {

        ADDITION("+", 2) {
            @Override
            public String compute(RpnConverter converter, String... args) throws InvalidRpnSyntaxException {
                return converter.reverseConvert(converter.convert(args[0]) + converter.convert(args[1]));
            }
        },
        SUBSTRACTION("-", 2) {
            @Override
            public String compute(RpnConverter converter, String... args) throws InvalidRpnSyntaxException {
                return converter.reverseConvert(converter.convert(args[0]) - converter.convert(args[1]));
            }
        },
        MULTIPLICATION("*", 2) {
            @Override
            public String compute(RpnConverter converter, String... args) throws InvalidRpnSyntaxException {
                return converter.reverseConvert(converter.convert(args[0]) * converter.convert(args[1]));
            }
        },
        DIVISION("/", 2) {
            @Override
            public String compute(RpnConverter converter, String... args) throws InvalidRpnSyntaxException {
                return converter.reverseConvert(converter.convert(args[0]) / converter.convert(args[1]));
            }
        },
        INCREMENT("++", 1) {
            @Override
            public String compute(RpnConverter converter, String... args) throws InvalidRpnSyntaxException {
                return converter.reverseConvert(converter.convert(args[0]) + 1);
            }
        },
        DECREMENT("--", 1) {
            @Override
            public String compute(RpnConverter converter, String... args) throws InvalidRpnSyntaxException {
                return converter.reverseConvert(converter.convert(args[0]) - 1);
            }
        },
        MODULO("%", 2) {
            @Override
            public String compute(RpnConverter converter, String... args) throws InvalidRpnSyntaxException {
                return converter.reverseConvert(converter.convert(args[0]) % converter.convert(args[1]));
            }
        };

        private final String symbol;
        private final Integer arity;

        RpnOperator(String symbol, Integer arity) {
            this.symbol = symbol;
            this.arity = arity;
        }

        Integer getArity() {
            return arity;
        }
    }

    private static RpnArithmeticOperator matchFirst(RpnConverter converter, List<String> tokens, Integer position) throws InvalidRpnSyntaxException {
        for (RpnOperator operator : RpnOperator.values()) {
            if (operator.symbol.equals(tokens.get(0))) {
                return new RpnArithmeticOperator(converter, operator, position);
            }
        }
        if (tokens.size() == 1) {
            return null;
        }
        return matchFirst(converter, tokens.subList(1, tokens.size()), ++position);
    }

    private List<String> evaluate(List<String> tokens) throws InvalidRpnSyntaxException {
        if (position != -1 && position < operator.getArity()) {
            throw new InvalidRpnSyntaxException(String.format("Insufficient argument count, expected %d, actual was %d", operator.getArity(), position));
        }
        List<String> newTokens = new ArrayList<>();
        if (position > operator.getArity()) {
            newTokens.addAll(tokens.subList(0, position - operator.getArity()));
        }

        newTokens.add(compute(tokens.subList(position - operator.getArity(), position).toArray(new String[operator.getArity()])));
        if (position < tokens.size() - 1) {
            newTokens.addAll(tokens.subList(position + 1, tokens.size()));
        }
        return evaluate(converter, newTokens);
    }

    static List<String> evaluate(RpnConverter converter, List<String> tokens) throws InvalidRpnSyntaxException {
        if (tokens.isEmpty()) {
            return tokens;
        }
        RpnArithmeticOperator operator = matchFirst(converter, tokens, 0);
        if (operator == null) {
            return tokens;
        }
        return operator.evaluate(tokens);
    }

    String compute(String... args) throws InvalidRpnSyntaxException {
        return operator.compute(converter, args);
    }
}
