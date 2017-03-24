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
    public void should_raise_exception_when_evaluating_an_expression_holding_an_unparsable_string_number() throws Exception {
        thrown.expect(InvalidRpnSyntaxException.class);
        rpnCalculator.reduce("1 1 2 3 a + - * /");
    }

    @Test
    public void should_compute_the_simplest_rpn_expression() throws Exception {
        assertThat(rpnCalculator.reduce("1"), is("1"));
    }

    @Test
    public void should_compute_a_basic_rpn_expression() throws Exception {
        assertThat(rpnCalculator.reduce("1 1 +"), is("2"));
    }

    @Test
    public void should_compute_a_complex_rpn_expression() throws Exception {
        assertThat(rpnCalculator.reduce("2 4 8 2 + 4 3 2 + 1 1 + * - * + *"), is("-112"));
    }

    @Test
    public void should_raise_exception_when_evaluating_an_operator_free_expression() throws Exception {
        assertThat(rpnCalculator.reduce("1 1"), is("1 1"));
    }

    @Test
    public void should_compute_partially_an_expression_holding_a_missing_symbol() throws Exception {
        assertThat(rpnCalculator.reduce("1 1 2 3 5 + - *"), is("1 -6"));
    }

    @Test
    public void should_compute_a_rpn_expression_rounding_up_result() throws Exception {
        assertThat(rpnCalculator.reduce("8 487 87 - 185 + 2 / *"), is("2336"));
    }

    @Test
    public void should_compute_a_rpn_expression_holding_increments() throws Exception {
        assertThat(rpnCalculator.reduce("2 3 -- * 2 1 ++ * +"), is("8"));
    }

    @Test
    public void should_compute_a_rpn_expression_holding_a_division_per_zero() throws Exception {
        assertThat(rpnCalculator.reduce("0 0 /"), is(String.valueOf(Long.MAX_VALUE)));
    }

    @Test
    public void should_filter_odd_numbers_successfully() throws Exception {
        assertThat(rpnCalculator.filter("0 1 2 3 4 F2%0="), is("0 2 4"));
    }

    @Test
    public void should_filter_even_numbers_successfully() throws Exception {
        assertThat(rpnCalculator.filter("0 1 2 3 4 F2%0!="), is("1 3"));
    }

    @Test
    public void should_filter_a_complex_expression() throws Exception {
        assertThat(rpnCalculator.filter("0 1 2 3 4 F2%0= 8 10 12 F2%0="), is("0 2 4 8 10 12"));
    }

}