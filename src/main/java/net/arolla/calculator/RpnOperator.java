package net.arolla.calculator;

public enum RpnOperator {

    ADDITION("+"),
    MULTIPLICATION("*"),
    SUBSTRACTION("-"),
    DIVISION("/");

    private final String symbol;

    RpnOperator(String symbol) {
        this.symbol = symbol;
    }


}
