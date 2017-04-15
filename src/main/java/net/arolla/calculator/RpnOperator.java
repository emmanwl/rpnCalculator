package net.arolla.calculator;

import java.util.List;

interface RpnOperator<E> {

    E operate(RpnEvaluationContext context, List<String> args) throws InvalidRpnSyntaxException;

}
