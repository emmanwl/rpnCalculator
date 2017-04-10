package net.arolla.calculator;

final class RpnComparisonOperator {

    private final RpnConverter converter;
    private final RpnOperator rpnOperator;

    RpnComparisonOperator(RpnConverter converter, String symbol) throws InvalidRpnSyntaxException {
        this.converter = converter;
        this.rpnOperator = RpnOperator.map(symbol);
    }

    private interface Compare {
        boolean compare(RpnConverter converter, String s, String t) throws InvalidRpnSyntaxException;
    }

    private enum RpnOperator implements Compare {

        EQUAL("=") {
            @Override
            public boolean compare(RpnConverter converter, String s, String t) throws InvalidRpnSyntaxException {
                return converter.convert(s).compareTo(converter.convert(t)) == 0;
            }
        },
        LOWER("<") {
            @Override
            public boolean compare(RpnConverter converter, String s, String t) throws InvalidRpnSyntaxException {
                return converter.convert(s).compareTo(converter.convert(t)) < 0;
            }
        },
        GREATER(">") {
            @Override
            public boolean compare(RpnConverter converter, String s, String t) throws InvalidRpnSyntaxException {
                return converter.convert(s).compareTo(converter.convert(t)) > 0;
            }
        },
        DIFFERENT("!=") {
            @Override
            public boolean compare(RpnConverter converter, String s, String t) throws InvalidRpnSyntaxException {
                return converter.convert(s).compareTo(converter.convert(t)) != 0;
            }
        },
        LOWER_THAN_OR_EQUAL("<=") {
            @Override
            public boolean compare(RpnConverter converter, String s, String t) throws InvalidRpnSyntaxException {
                return converter.convert(s).compareTo(converter.convert(t)) <= 0;
            }
        },
        GREATER_THAN_OR_EQUAL(">=") {
            @Override
            public boolean compare(RpnConverter converter, String s, String t) throws InvalidRpnSyntaxException {
                return converter.convert(s).compareTo(converter.convert(t)) >= 0;
            }
        };

        private final String symbol;

        RpnOperator(String symbol) {
            this.symbol = symbol;
        }

        static RpnOperator map(String symbol) throws InvalidRpnSyntaxException {
            for (RpnOperator operator : values()) {
                if (operator.symbol.equals(symbol)) {
                    return operator;
                }
            }
            throw new InvalidRpnSyntaxException("Could not map comparison symbol " + symbol);
        }
    }

    boolean compare(String s, String t) throws InvalidRpnSyntaxException {
        return rpnOperator.compare(converter, s, t);
    }
}
