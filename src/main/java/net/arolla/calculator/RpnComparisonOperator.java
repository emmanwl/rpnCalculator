package net.arolla.calculator;

public enum RpnComparisonOperator implements Comparator<String> {

	EQUAL("=") {
		@Override
		public boolean compare(String s, String s2) throws InvalidRpnSyntaxException {
			return longValue(s) == longValue(s2);
		}
	},
	LOWER("<") {
		@Override
		public boolean compare(String s, String s2) throws InvalidRpnSyntaxException {
			return longValue(s) < longValue(s2);
		}
	},
	GREATER(">") {
		@Override
		public boolean compare(String s, String s2) throws InvalidRpnSyntaxException {
			return longValue(s) > longValue(s2);
		}
	},
	DIFFERENT("!=") {
		@Override
		public boolean compare(String s, String s2) throws InvalidRpnSyntaxException {
			return longValue(s) != longValue(s2);
		}
	},
	LOWER_THAN_OR_EQUAL("<=") {
		@Override
		public boolean compare(String s, String s2) throws InvalidRpnSyntaxException {
			return longValue(s) <= longValue(s2);
		}
	},
	GREATER_THAN_OR_EQUAL(">=") {
		@Override
		public boolean compare(String s, String s2) throws InvalidRpnSyntaxException {
			return longValue(s) >= longValue(s2);
		}
	};

	private final String symbol;

	RpnComparisonOperator(String symbol) {
		this.symbol = symbol;
	}

	private static Long longValue(String number) throws InvalidRpnSyntaxException {
		try {
			return Long.valueOf(number);
		} catch (NumberFormatException e) {
			throw new InvalidRpnSyntaxException(e.getMessage());
		}
	}

	public static RpnComparisonOperator matchSymbol(String symbol) throws InvalidRpnSyntaxException {
		for (RpnComparisonOperator op : values()) {
			if (op.symbol.equals(symbol)) {
				return op;
			}
		}
		throw new InvalidRpnSyntaxException("Could not match comparison symbol " + symbol);
	}
}
