package net.arolla.calculator;

import java.util.ArrayList;
import java.util.List;

public class RpnReductionOperator implements Reducer<String> {

    private final RpnArithmeticOperator rpnArithmeticOperator;
    private final Integer position;

    private RpnReductionOperator(RpnArithmeticOperator rpnArithmeticOperator, Integer position) {
        this.rpnArithmeticOperator = rpnArithmeticOperator;
        this.position = position;
    }

    public RpnReductionOperator(RpnArithmeticOperator rpnArithmeticOperator) {
       this(rpnArithmeticOperator, -1);
    }

    private static RpnReductionOperator matchFirst(List<String> tokens, Integer position) {
        if (tokens.isEmpty()) {
            return null;
        }
        for (RpnArithmeticOperator op : RpnArithmeticOperator.values()) {
            if (op.getSymbol().equals(tokens.get(0))) {
                return new RpnReductionOperator(op, position);
            }
        }
        return matchFirst(tokens.subList(1, tokens.size()), ++position);
    }

    private List<String> evaluateWithOperator(List<String> tokens) throws InvalidRpnSyntaxException {
        if (position != -1 && position < rpnArithmeticOperator.getArity()) {
            throw new InvalidRpnSyntaxException(String.format("Insufficient argument count, expected %d, actual was %d", rpnArithmeticOperator.getArity(), position));
        }
        List<String> newTokens = new ArrayList<>();
        if (position > rpnArithmeticOperator.getArity()) {
            newTokens.addAll(tokens.subList(0, position - rpnArithmeticOperator.getArity()));
        }
        newTokens.add(reduce(tokens.subList(position - rpnArithmeticOperator.getArity(), position).toArray(new String[rpnArithmeticOperator.getArity()])));
        if (position < tokens.size() - 1) {
            newTokens.addAll(tokens.subList(position + 1, tokens.size()));
        }
        return evaluate(newTokens);
    }

    public static List<String> evaluate(List<String> tokens) throws InvalidRpnSyntaxException {
        if (tokens.size() <= 1) {
            return tokens;
        }
        RpnReductionOperator operator = matchFirst(tokens, 0);
        if (operator == null) {
            return tokens;
        }
        return operator.evaluateWithOperator(tokens);
    }

    @Override
    public String reduce(String... args) throws InvalidRpnSyntaxException {
        return rpnArithmeticOperator.reduce(args);
    }
}
