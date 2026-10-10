package com.example

import com.example.arccalc.engine.CalculatorEngine
import com.example.arccalc.engine.UnitConverterEngine
import com.example.arccalc.model.AngleMode
import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun testOperatorPrecedence() {
        val result = CalculatorEngine.evaluate("2 + 3 × 4")
        assertTrue(result is CalculatorEngine.EvalResult.Success)
        assertEquals("14", (result as CalculatorEngine.EvalResult.Success).formatted)
    }

    @Test
    fun testParentheses() {
        val r1 = CalculatorEngine.evaluate("(2 + 3) × 4")
        assertTrue(r1 is CalculatorEngine.EvalResult.Success)
        assertEquals("20", (r1 as CalculatorEngine.EvalResult.Success).formatted)

        val r2 = CalculatorEngine.evaluate("2 × (3 + 4)")
        assertTrue(r2 is CalculatorEngine.EvalResult.Success)
        assertEquals("14", (r2 as CalculatorEngine.EvalResult.Success).formatted)
    }

    @Test
    fun testTrigonometryDegrees() {
        val result = CalculatorEngine.evaluate("sin(30)", AngleMode.DEG)
        assertTrue(result is CalculatorEngine.EvalResult.Success)
        assertEquals("0.5", (result as CalculatorEngine.EvalResult.Success).formatted)
    }

    @Test
    fun testSquareRootAndPower() {
        val rSqrt = CalculatorEngine.evaluate("√(16)")
        assertTrue(rSqrt is CalculatorEngine.EvalResult.Success)
        assertEquals("4", (rSqrt as CalculatorEngine.EvalResult.Success).formatted)

        val rPow = CalculatorEngine.evaluate("2 ^ 3")
        assertTrue(rPow is CalculatorEngine.EvalResult.Success)
        assertEquals("8", (rPow as CalculatorEngine.EvalResult.Success).formatted)
    }

    @Test
    fun testFactorial() {
        val result = CalculatorEngine.evaluate("5!")
        assertTrue(result is CalculatorEngine.EvalResult.Success)
        assertEquals("120", (result as CalculatorEngine.EvalResult.Success).formatted)
    }

    @Test
    fun testDivisionByZero() {
        val result = CalculatorEngine.evaluate("1 ÷ 0")
        assertTrue(result is CalculatorEngine.EvalResult.Error)
        assertEquals("Cannot divide by 0", (result as CalculatorEngine.EvalResult.Error).message)
    }

    @Test
    fun testUnitConverterLength() {
        val converted = UnitConverterEngine.convert("length", "m", "ft", 1.0)
        assertEquals(3.280839895, converted, 0.001)
    }

    @Test
    fun testUnitConverterTemperature() {
        val converted = UnitConverterEngine.convert("temperature", "c", "f", 100.0)
        assertEquals(212.0, converted, 0.001)
    }
}
