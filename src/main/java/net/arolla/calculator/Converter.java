package net.arolla.calculator;

interface Converter<N extends Number> {

    N convert(String s) throws InvalidRpnSyntaxException;

    String reverseConvert(N n) throws InvalidRpnSyntaxException;
}
