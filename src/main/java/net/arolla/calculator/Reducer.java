package net.arolla.calculator;

interface Reducer<T> {

    T reduce(T... args) throws InvalidRpnSyntaxException;

}
