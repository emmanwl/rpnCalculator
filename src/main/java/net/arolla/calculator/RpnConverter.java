package net.arolla.calculator;

interface RpnConverter<N extends Number> {

    N convert(String s) throws InvalidRpnSyntaxException;

    String reverseConvert(N n);

    final class RpnLongConverter implements RpnConverter<Long> {

        @Override
         public Long convert(String s) throws InvalidRpnSyntaxException {
            try {
                return Long.valueOf(s);
            } catch (NumberFormatException e) {
                throw new InvalidRpnSyntaxException(e);
            }
        }

        @Override
        public String reverseConvert(Long l) {
            return String.valueOf(l);
        }
    }
}
