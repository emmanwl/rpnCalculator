package net.arolla.calculator;

interface Reduce<T> {

    T reduce(T... args) throws InvalidRpnSyntaxException;

}
