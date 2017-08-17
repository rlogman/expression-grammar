package calculator;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class CalculatorGrammarTest {
  @Rule
  public ExpectedException thrown = ExpectedException.none();
  
  @Test
  public void testNumber() throws ParsingException {
    Integer result = CalculatorGrammar.parse("6");
    assertEquals("Error with simple number", Integer.valueOf(6), result);
  }

  @Test
  public void testVariable() throws ParsingException {
    Integer result = CalculatorGrammar.parse("n");
    assertNull("Error with missing variable", result);
  }

  @Test
  public void testSimpleLet() throws ParsingException {
    Integer result = CalculatorGrammar.parse("let(b, 5, b)");
    assertEquals("Error with simple let", Integer.valueOf(5), result);
  }

  @Test
  public void testVariableWithExpression() throws ParsingException {
    Integer result = CalculatorGrammar.parse("let(b, 5, mult(b, 4))");
    assertEquals("Error with let involving an expression", Integer.valueOf(20), result);
  }

  @Test
  public void testAddition() throws ParsingException {
    Integer result = CalculatorGrammar.parse("add(8, 9)");
    assertEquals("Error with simple addition", Integer.valueOf(17), result);
  }

  @Test
  public void testSubstraction() throws ParsingException {
    Integer result = CalculatorGrammar.parse("sub(8, 9)");
    assertEquals("Error with simple substaction", Integer.valueOf(-1), result);
  }

  @Test
  public void testMultiplication() throws ParsingException {
    Integer result = CalculatorGrammar.parse("mult(5, 7)");
    assertEquals("Error with simple multiplication", Integer.valueOf(35), result);
  }

  @Test
  public void testDivision() throws ParsingException {
    Integer result = CalculatorGrammar.parse("div(49, 8)");
    assertEquals("Error with simple division", Integer.valueOf(6), result);
  }

  @Test
  public void testDivByZero() throws ParsingException {
    thrown.expect(ParsingException.class);
    thrown.expectMessage(CalculatorGrammar.GENERAL_PARSING_EXCEPTION);
    thrown.expectCause(instanceOf(ArithmeticException.class));
    CalculatorGrammar.parse("div(5, 0)");
  }

  @Test
  public void testEmptyInput() throws ParsingException {
    thrown.expect(ParsingException.class);
    thrown.expectMessage(CalculatorGrammar.GENERAL_PARSING_EXCEPTION);
    thrown.expectCause(instanceOf(ParsingException.class));
    CalculatorGrammar.parse("");
  }

  @Test
  public void testNullInput() throws ParsingException {
    thrown.expect(ParsingException.class);
    thrown.expectMessage(CalculatorGrammar.GENERAL_PARSING_EXCEPTION);
    thrown.expectCause(instanceOf(NullPointerException.class));
    CalculatorGrammar.parse(null);
  }

  @Test
  public void testWrongInput() throws ParsingException {
    thrown.expect(ParsingException.class);
    thrown.expectMessage(CalculatorGrammar.GENERAL_PARSING_EXCEPTION);
    thrown.expectCause(instanceOf(ParsingException.class));
    CalculatorGrammar.parse("let(a, let(b, 10, add(b, b)), l");
  }

  @Test
  public void testComplexExpression() throws ParsingException {
    Integer result = CalculatorGrammar.parse("let(a, 5, let(b, mult(a, 10), add(b, a)))");
    assertEquals("Error with complex operation", Integer.valueOf(55), result);
  }

  @Test
  public void testAnotherComplexOperation() throws ParsingException {
    Integer result = CalculatorGrammar.parse("let(a, let(b, 10, add(b, b)), let(b, 20, add(a, b)))");
    assertEquals("Error with complex operation", Integer.valueOf(40), result);
  }

  @Test
  public void testSimpleReferenceToA() throws ParsingException {
    Integer result = CalculatorGrammar.parse("a");
    assertNull("Error with missing variable called a (special case because of partial collision with token add)", result);
  }

}
