package com.aplazo.mx.creditlines.business;
import java.math.BigDecimal;
import java.util.Optional;

public interface CreditLineCalculatorStrategy {
    Optional<Double> calculateCreditLine(int age);
}
