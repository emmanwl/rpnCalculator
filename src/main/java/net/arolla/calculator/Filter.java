package net.arolla.calculator;

import java.util.List;

interface Filter<T> {

    List<T> filter(List<T> items) throws InvalidRpnSyntaxException;

}
