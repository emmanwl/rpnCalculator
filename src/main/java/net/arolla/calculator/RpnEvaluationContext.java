package net.arolla.calculator;

import net.arolla.calculator.RpnConverter.RpnLongConverter;

import java.util.regex.Matcher;

class RpnEvaluationContext {

    private final RpnLongConverter converter;
    private final Matcher matcher;

    private RpnEvaluationContext(RpnEvaluationContextBuilder builder) {
        this.converter = builder.converter;
        this.matcher = builder.matcher;
    }

    RpnLongConverter getRpnConverter() {
        return converter;
    }

    Matcher getMatcher() {
        return matcher;
    }

    static class RpnEvaluationContextBuilder {

        private final RpnLongConverter converter;
        private Matcher matcher;

        private RpnEvaluationContextBuilder(RpnLongConverter converter) {
            this.converter = converter;
        }

        RpnEvaluationContextBuilder withMatcher(Matcher matcher) {
            this.matcher = matcher;
            return this;
        }

        RpnEvaluationContext build() {
            return new RpnEvaluationContext(this);
        }
    }

    static RpnEvaluationContextBuilder builder(RpnLongConverter converter) {
        return new RpnEvaluationContextBuilder(converter);
    }
}
