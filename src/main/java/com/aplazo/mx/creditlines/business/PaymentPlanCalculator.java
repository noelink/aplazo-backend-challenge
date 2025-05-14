package com.aplazo.mx.creditlines.business;

import com.aplazo.mx.creditlines.enums.InstallmentStatus;
import com.aplazo.mx.creditlines.repository.entity.Customer;
import com.aplazo.mx.creditlines.repository.entity.Installment;
import com.aplazo.mx.creditlines.repository.entity.PaymentPlan;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class PaymentPlanCalculator {

    public PaymentPlan createPaymentPlan(Customer customer, int numberOfInstallments, Double amount) {
        String firstName = customer.getFirstName();
        UUID idClient = customer.getIdClient();
        PaymentPlan paymentPlan = new PaymentPlan();
        boolean startsWithCLH = firstName != null && (
                firstName.startsWith("C") ||
                        firstName.startsWith("L") ||
                        firstName.startsWith("H")
        );

        boolean uuidGreaterThan25 = idClient.toString().length() > 25;

        if (startsWithCLH) {
            paymentPlan.setCommissionAmount(13.0);
        } else if (uuidGreaterThan25) {
            paymentPlan.setCommissionAmount(16.0);
        } else {
            paymentPlan.setCommissionAmount(16.0);
        }
        paymentPlan.setInstallments(createInstallments(numberOfInstallments, paymentPlan.getCommissionAmount(), amount));
        return paymentPlan;
    }

    private List<Installment> createInstallments(int numberOfInstallments, Double commissionAmount, Double amount) {
        double interestRate = amount * (commissionAmount / 100);
        double paymentPerInstallment = (amount + interestRate) / numberOfInstallments;
        return IntStream.range(0, numberOfInstallments)
                .mapToObj(i -> createInstallment(paymentPerInstallment))
                .collect(Collectors.toList());
    }

    private Installment createInstallment(double paymentPerInstallment) {
        return Installment.builder()
                .amount(paymentPerInstallment)
                .scheduledPaymentDate(LocalDate.now())
                .status(InstallmentStatus.NEXT)
                .build();
    }
}
