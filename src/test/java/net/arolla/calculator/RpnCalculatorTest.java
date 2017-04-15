package net.arolla.calculator;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

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
        rpnCalculator.compute("1 1 2 3 a + - * /");
    }

    @Test
    public void should_compute_the_simplest_rpn_expression() throws Exception {
        assertThat(rpnCalculator.compute("1"), is("1"));
    }

    @Test
    public void should_compute_a_basic_rpn_expression() throws Exception {
        assertThat(rpnCalculator.compute("1 1 +"), is("2"));
    }

    @Test
    public void should_compute_a_complex_rpn_expression() throws Exception {
        assertThat(rpnCalculator.compute("2 4 8 2 + 4 3 2 + 1 1 + * - * + *"), is("-112"));
    }

    @Test
    public void should_return_an_operator_free_expression() throws Exception {
        assertThat(rpnCalculator.compute("1 1 5 6"), is("1 1 5 6"));
    }

    @Test
    public void should_compute_partially_an_expression_holding_a_missing_operator() throws Exception {
        assertThat(rpnCalculator.compute("1 1 2 3 5 + - *"), is("1 -6"));
    }

    @Test
    public void should_raise_an_exception_when_operator_is_not_provided_with_enough_arguments() throws Exception {
        thrown.expect(InvalidRpnSyntaxException.class);
        thrown.expectMessage("Insufficient argument count, expected 2, actual was 1");
        rpnCalculator.compute("1 +");
    }

    @Test
    public void should_compute_a_rpn_expression_rounding_up_result() throws Exception {
        assertThat(rpnCalculator.compute("8 487 87 - 185 + 2 / *"), is("2336"));
    }

    @Test
    public void should_compute_a_rpn_expression_with_increments() throws Exception {
        assertThat(rpnCalculator.compute("2 3 -- * 2 1 ++ * +"), is("8"));
    }

    @Ignore
    public void should_not_compute_a_rpn_expression_holding_a_division_per_zero() throws Exception {
        thrown.expect(ArithmeticException.class);
        assertThat(rpnCalculator.compute("0 0 /"), is(String.valueOf(Long.MAX_VALUE)));
    }

    @Test
    public void should_filter_odd_numbers_successfully() throws Exception {
        assertThat(rpnCalculator.reduce("0 1 2 3 4 F2%0="), is("0 2 4"));
    }

    @Test
    public void should_filter_even_numbers_successfully() throws Exception {
        assertThat(rpnCalculator.reduce("0 1 2 3 4 F2%0!="), is("1 3"));
    }

    @Test
    public void should_filter_a_complex_expression() throws Exception {
        assertThat(rpnCalculator.reduce("0 1 2 3 4 F2%0= 8 10 12 F2%0="), is("0 2 4 8 10 12"));
    }

    @Test
    public void should_filter_an_operand_free_expression() throws Exception {
        assertThat(rpnCalculator.reduce("F2%0="), is(""));
    }

    @Test
    public void should_map_a_simple_expression() throws Exception {
        assertThat(rpnCalculator.reduce("0 1 2 3 4 M2*"), is("0 2 4 6 8"));
    }

    @Test
    public void should_map_an_operand_free_expression() throws Exception {
        assertThat(rpnCalculator.reduce("M2*"), is(""));
    }

    @Test
    public void should_reduce_an_operand_free_expression() throws Exception {
        assertThat(rpnCalculator.reduce("F2%0= M2*"), is(""));
    }
}