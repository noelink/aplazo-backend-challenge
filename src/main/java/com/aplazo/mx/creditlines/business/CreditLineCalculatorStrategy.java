package com.aplazo.mx.creditlines.business;

import java.util.Optional;

public interface CreditLineCalculatorStrategy {
    Optional<Double> calculateCreditLine(int age);
}
