import com.rest.application.Calculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalculatorTest {
    Calculator calculator;

    @BeforeEach
    public void setUp() {
        calculator = new Calculator();
    }

    @Test
    public void testMultiply() {
        assertEquals(20, calculator.multiply(5 , 4));
    }

    @Test
    public void testDivide(){
        assertEquals(5,calculator.divide(25,5));
    }
}
