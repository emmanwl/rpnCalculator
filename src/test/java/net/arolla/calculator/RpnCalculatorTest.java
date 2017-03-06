package net.arolla.calculator;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class RpnCalculatorTest {

    public RpnCalculator rpnCalculator;

    @Before
    public void setUp() throws Exception {
        rpnCalculator = new RpnCalculator();
    }

    @Test
    public void should_compute_simplest_rpn_expression_successfully() throws Exception {
        assertThat(rpnCalculator.evaluate("1 1 +"), is("2"));
    }

    @Test
    public void should_compute_nested_rpn_expression_successfully() throws Exception {
        assertThat(rpnCalculator.evaluate("8 2 + 4 3 2 + 1 1 + * - *"), is("-60"));
    }
}