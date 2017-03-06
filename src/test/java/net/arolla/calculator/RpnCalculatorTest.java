package net.arolla.calculator;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class RpnCalculatorTest {

    private RpnCalculator rpnCalculator;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        rpnCalculator = new RpnCalculator();
    }

    @Test
    public void should_raise_exception_when_evaluating_an_expression_holding_a_surnumerary_symbol() throws Exception {
        thrown.expect(InvalidRpnSyntaxException.class);
        rpnCalculator.evaluate("1 1 2 3 5 + - *");
    }

    @Test
    public void should_raise_exception_when_evaluating_an_expression_holding_an_unparsable_string_number() throws Exception {
        thrown.expect(InvalidRpnSyntaxException.class);
        rpnCalculator.evaluate("1 1 2 3 a + - * /");
    }

    @Test
    public void should_compute_the_simplest_rpn_expression_successfully() throws Exception {
        assertThat(rpnCalculator.evaluate("1"), is("1"));
    }

    @Test
    public void should_compute_a_basic_rpn_expression_successfully() throws Exception {
        assertThat(rpnCalculator.evaluate("1 1 +"), is("2"));
    }

    @Test
    public void should_compute_a_complex_rpn_expression_successfully() throws Exception {
        assertThat(rpnCalculator.evaluate("2 4 8 2 + 4 3 2 + 1 1 + * - * + *"), is("-112"));
    }

    @Test
    public void should_compute_a_not_so_basic_rpn_expression_successfully() throws Exception {
        assertThat(rpnCalculator.evaluate("8 487 87 - 185 + 2 / *"), is("2336"));
    }
}