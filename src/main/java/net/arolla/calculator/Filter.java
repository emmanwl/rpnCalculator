package net.arolla.calculator;

import java.util.List;

interface Filter<T> {

    List<T> filter(int index, List<T> items) throws InvalidRpnSyntaxException;

    boolean matches(String token);

}
