package net.arolla.calculator;

interface RpnConverter<E, S> {

    S operand(E e) throws InvalidRpnSyntaxException ;

    E token(S s);

    class RpnStringLongConverter implements RpnConverter<String, Long> {

        @Override
        public Long operand(String s) throws InvalidRpnSyntaxException {
            try {
                return Long.valueOf(s);
            } catch (NumberFormatException e) {
                throw new InvalidRpnSyntaxException(e.getMessage());
            }
        }

        @Override
        public String token(Long l) {
            return String.valueOf(l);
        }
    }

}
