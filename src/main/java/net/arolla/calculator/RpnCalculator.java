package net.arolla.calculator;


import com.google.common.base.Joiner;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Arrays.asList;

public class RpnCalculator {

    private static final String RPN_TOKEN_SEPARATOR = " ";

    public String evaluate(String expression) throws InvalidRpnSyntaxException {
        checkNotNull(expression);
        RpnExpressionParser parser = new RpnExpressionParser(RpnOperator.values());
        List<String> reduced = parser.reduce(asList(expression.split(RPN_TOKEN_SEPARATOR)));
        return Joiner.on(RPN_TOKEN_SEPARATOR).join(reduced);
    }

}
