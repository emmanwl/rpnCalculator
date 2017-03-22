package net.arolla.calculator;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

public enum RpnFixedArityOperator implements Reduce<String> {

    ADDITION("+", 2) {
        @Override
        public String reduce(String... args) throws InvalidRpnSyntaxException {
            return String.valueOf(longValue(args[0]) + longValue(args[1]));
        }
    },
    SUBSTRACTION("-", 2) {
        @Override
        public String reduce(String... args) throws InvalidRpnSyntaxException {
            return String.valueOf(longValue(args[0]) - longValue(args[1]));
        }
    },
    MULTIPLICATION("*", 2) {
        @Override
        public String reduce(String... args) throws InvalidRpnSyntaxException {
            return String.valueOf(longValue(args[0]) * longValue(args[1]));
        }
    },
    DIVISION("/", 2) {
        @Override
        public String reduce(String... args) throws InvalidRpnSyntaxException {
            Long numerator = longValue(args[0]);
            Long denominator = longValue(args[1]);
            if (denominator == 0) {
                return String.valueOf(Long.signum(numerator) == -1 ? Long.MIN_VALUE : Long.MAX_VALUE);
            }
            return String.valueOf(numerator / denominator);
        }
    },
    INCREMENT("++", 1) {
        @Override
        public String reduce(String... args) throws InvalidRpnSyntaxException {
            return String.valueOf(longValue(args[0]) + 1);
        }
    },
    DECREMENT("--", 1) {
        @Override
        public String reduce(String... args) throws InvalidRpnSyntaxException {
            return String.valueOf(longValue(args[0]) - 1);
        }
    },
    MODULO("%", 2) {
        @Override
        public String reduce(String... args) throws InvalidRpnSyntaxException {
            return String.valueOf(longValue(args[0]) % longValue(args[1]));
        }
    };

    private final String symbol;
    private final Integer arity;

    RpnFixedArityOperator(String symbol, Integer arity) {
        checkArgument(symbol != null && !symbol.isEmpty(), "Operator symbol cannot be empty");
        checkArgument(arity > 0, "Operator arity must be greater than 0");
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

    public Integer getArity() {
        return arity;
    }

    public static RpnFixedArityOperator matchFirst(List<String> tokens) {
        if (tokens.isEmpty()) {
            return null;
        }
        for (RpnFixedArityOperator operator : values()) {
            if (operator.symbol.equals(tokens.get(0))) {
                return operator;
            }
        }
        return matchFirst(tokens.subList(1, tokens.size()));
    }

    public Integer getSymbolIndex(List<String> tokens) throws InvalidRpnSyntaxException {
        Integer index = tokens.indexOf(symbol);
        if (index != -1 && index < arity) {
            throw new InvalidRpnSyntaxException(String.format("Insufficient argument count, expected %d, actual was %d", arity, index));
        }
        return index;
    }

    public List<String> apply(String operand, List<String> tokens) throws InvalidRpnSyntaxException {
        List<String> results = new ArrayList<>();
        for (int i = 0; i < tokens.size(); i++) {
            results.add(this.reduce(tokens.get(i), operand));
        }
        return results;
    }
}
