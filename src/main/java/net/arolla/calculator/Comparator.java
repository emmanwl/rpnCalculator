package net.arolla.calculator;

interface Comparator<T> {

    boolean compare(T t, T s) throws InvalidRpnSyntaxException;
}
