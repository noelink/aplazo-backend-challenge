package com.aplazo.mx.creditlines.business;


import com.aplazo.mx.creditlines.enums.InstallmentStatus;
import com.aplazo.mx.creditlines.repository.entity.Customer;
import com.aplazo.mx.creditlines.repository.entity.Installment;
import com.aplazo.mx.creditlines.repository.entity.PaymentPlan;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class PaymentPlanCalculatorTest {

    private PaymentPlanCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new PaymentPlanCalculator();
    }

    @Test
    void testCreatePaymentPlan_FirstNameStartsWithC_Commission13Percent() {
        Customer customer = createCustomer("Carlos", UUID.randomUUID());
        double amount = 10000.0;
        int installments = 4;

        PaymentPlan plan = calculator.createPaymentPlan(customer, installments, amount);

        assertNotNull(plan);
        assertEquals(1300.0, plan.getCommissionAmount());
        assertInstallments(plan.getInstallments(), installments, amount, 1300.0);
    }

    @Test
    void testCreatePaymentPlan_FirstNameStartsWithL_Commission13Percent() {
        Customer customer = createCustomer("Laura", UUID.randomUUID());
        double amount = 20000.0;
        int installments = 2;

        PaymentPlan plan = calculator.createPaymentPlan(customer, installments, amount);

        assertEquals(2600.0, plan.getCommissionAmount());
        assertInstallments(plan.getInstallments(), installments, amount, 2600.0);
    }

    @Test
    void testCreatePaymentPlan_FirstNameStartsWithH_Commission13Percent() {
        Customer customer = createCustomer("Hugo", UUID.randomUUID());
        double amount = 30000.0;
        int installments = 3;

        PaymentPlan plan = calculator.createPaymentPlan(customer, installments, amount);

        assertEquals(3900.0, plan.getCommissionAmount());
        assertInstallments(plan.getInstallments(), installments, amount, 3900.0);
    }

    @Test
    void testCreatePaymentPlan_FirstNameNotCLH_UUIDLengthGreaterThan25_Commission16Percent() {
        // UUID con longitud > 25 garantizada
        UUID uuid = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        Customer customer = createCustomer("Pedro", uuid);
        double amount = 10000.0;
        int installments = 5;

        PaymentPlan plan = calculator.createPaymentPlan(customer, installments, amount);

        assertEquals(1600.0, plan.getCommissionAmount());
        assertInstallments(plan.getInstallments(), installments, amount, 1600.0);
    }

    @Test
    void testCreatePaymentPlan_FirstNameNotCLH_UUIDLengthLessThan25_Commission16Percent() {
        // UUID con longitud < 25 (truncado manualmente para testear)
        UUID uuid = UUID.nameUUIDFromBytes("short".getBytes()); // genera un UUID con longitud < 25
        Customer customer = createCustomer("Pedro", uuid);
        double amount = 5000.0;
        int installments = 2;

        PaymentPlan plan = calculator.createPaymentPlan(customer, installments, amount);

        assertEquals(800.0, plan.getCommissionAmount());
        assertInstallments(plan.getInstallments(), installments, amount, 800.0);
    }

    // Métodos de apoyo

    private Customer createCustomer(String firstName, UUID uuid) {
        Customer customer = new Customer();
        customer.setFirstName(firstName);
        customer.setIdClient(uuid);
        customer.setLastName("Test");
        customer.setSecondLastName("Test");
        customer.setDateOfBirth(LocalDate.of(1990, 1, 1));
        customer.setCreditLineAmount(10000.0);
        customer.setCreationDate(LocalDate.now());
        customer.setUpdatedAt(LocalDate.now());
        return customer;
    }

    private void assertInstallments(List<Installment> installments, int expectedCount, double amount, double commission) {
        assertNotNull(installments);
        assertEquals(expectedCount, installments.size());

        double expectedPerInstallment = (amount + commission) / expectedCount;

        for (int i = 0; i < installments.size(); i++) {
            Installment inst = installments.get(i);
            assertEquals(expectedPerInstallment, inst.getAmount(), 0.001);
            assertEquals(InstallmentStatus.NEXT, inst.getStatus());
            assertEquals(LocalDate.now().plusDays(15L * (i + 1)), inst.getScheduledPaymentDate());
        }
    }
}
