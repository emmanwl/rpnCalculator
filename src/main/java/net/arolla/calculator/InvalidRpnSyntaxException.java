package net.arolla.calculator;

public class InvalidRpnSyntaxException extends Exception {

    public InvalidRpnSyntaxException(String message) {
        super(message);
    }

    public InvalidRpnSyntaxException(Throwable cause) {
        super(cause);
    }
}
