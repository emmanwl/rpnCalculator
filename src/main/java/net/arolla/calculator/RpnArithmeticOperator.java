package net.arolla.calculator;

import static com.google.common.base.Preconditions.checkArgument;

public enum RpnArithmeticOperator implements Reducer<String> {

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

    RpnArithmeticOperator(String symbol, Integer arity) {
        checkArgument(symbol != null && !symbol.isEmpty(), "Operator symbol cannot be empty");
        checkArgument(arity > 0, "Operator arity must be greater than 0");
        this.symbol = symbol;
        this.arity = arity;
    }

    public Integer getArity() {
        return arity;
    }

    public String getSymbol() {
        return symbol;
    }

    private static Long longValue(String number) throws InvalidRpnSyntaxException {
        try {
            return Long.valueOf(number);
        } catch (NumberFormatException e) {
            throw new InvalidRpnSyntaxException(e.getMessage());
        }
    }

    public static RpnArithmeticOperator matchSymbol(String symbol) throws InvalidRpnSyntaxException {
        for (RpnArithmeticOperator op : values()) {
            if (op.getSymbol().equals(symbol)) {
                return op;
            }
        }
        throw new InvalidRpnSyntaxException("Could not match reduction symbol " + symbol);
    }
}
