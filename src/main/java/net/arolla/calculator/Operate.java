package net.arolla.calculator;

interface Operate<T> {

    T operate(T... args) throws InvalidRpnSyntaxException;

}
