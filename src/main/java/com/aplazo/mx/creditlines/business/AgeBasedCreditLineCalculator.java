package com.aplazo.mx.creditlines.business;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;

@Component
@Slf4j
public class AgeBasedCreditLineCalculator implements CreditLineCalculatorStrategy{

    @Override
    public Optional<BigDecimal> calculateCreditLine(int age) {
        if (age >= 18 && age <= 25) {
            log.info("Age:{} - Approved credit line for $3000", age);
            return Optional.of(new BigDecimal("3000"));
        } else if (age >= 26 && age <= 30) {
            log.info("Age:{} - Approved credit line for $5000", age);
            return Optional.of(new BigDecimal("5000.00"));
        } else if (age >= 31 && age <= 65) {
            log.info("Age:{} - Approved credit line for $8000", age);
            return Optional.of(new BigDecimal("8000.00"));
        } else {
            log.info("Age:{} - Rejected credit line request", age);
            return Optional.empty();
        }
    }
}
