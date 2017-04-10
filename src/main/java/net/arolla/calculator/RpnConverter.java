package net.arolla.calculator;

final class RpnConverter implements Converter<Long> {

    @Override
     public Long convert(String s) throws InvalidRpnSyntaxException {
        try {
            return Long.valueOf(s);
        } catch (NumberFormatException e) {
            throw new InvalidRpnSyntaxException(e);
        }
    }

    @Override
    public String reverseConvert(Long l) throws InvalidRpnSyntaxException {
        return String.valueOf(l);
    }
}
