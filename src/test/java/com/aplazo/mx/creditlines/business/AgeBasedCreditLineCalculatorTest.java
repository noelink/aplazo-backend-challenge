package com.aplazo.mx.creditlines.business;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class AgeBasedCreditLineCalculatorTest {
    private AgeBasedCreditLineCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new AgeBasedCreditLineCalculator();
    }

    @Test
    void testCalculateCreditLine_AgeBetween18And25_Returns3000() {
        Optional<Double> result = calculator.calculateCreditLine(20);

        assertTrue(result.isPresent());
        assertEquals(3000.00, result.get());
    }

    @Test
    void testCalculateCreditLine_AgeBetween26And30_Returns5000() {
        Optional<Double> result = calculator.calculateCreditLine(28);

        assertTrue(result.isPresent());
        assertEquals(5000.00, result.get());
    }

    @Test
    void testCalculateCreditLine_AgeBetween31And65_Returns8000() {
        Optional<Double> result = calculator.calculateCreditLine(50);

        assertTrue(result.isPresent());
        assertEquals(8000.00, result.get());
    }

    @Test
    void testCalculateCreditLine_AgeBelow18_ReturnsEmpty() {
        Optional<Double> result = calculator.calculateCreditLine(16);

        assertTrue(result.isEmpty());
    }

    @Test
    void testCalculateCreditLine_AgeAbove65_ReturnsEmpty() {
        Optional<Double> result = calculator.calculateCreditLine(70);

        assertTrue(result.isEmpty());
    }

    @Test
    void testCalculateCreditLine_ExactBoundaries() {
        assertEquals(3000.00, calculator.calculateCreditLine(18).get());
        assertEquals(3000.00, calculator.calculateCreditLine(25).get());

        assertEquals(5000.00, calculator.calculateCreditLine(26).get());
        assertEquals(5000.00, calculator.calculateCreditLine(30).get());

        assertEquals(8000.00, calculator.calculateCreditLine(31).get());
        assertEquals(8000.00, calculator.calculateCreditLine(65).get());
    }
}
