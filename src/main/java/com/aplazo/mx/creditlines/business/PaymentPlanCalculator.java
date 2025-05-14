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
            paymentPlan.setCommissionAmount(amount * (13.0/100));
        } else if (uuidGreaterThan25) {
            paymentPlan.setCommissionAmount(amount * (16.0/100));
        } else {
            paymentPlan.setCommissionAmount(amount * (16.0/100));
        }
        paymentPlan.setInstallments(createInstallments(numberOfInstallments, paymentPlan.getCommissionAmount(), amount));
        return paymentPlan;
    }

    private List<Installment> createInstallments(int numberOfInstallments, Double commissionAmount, Double amount) {
        double paymentPerInstallment = (amount + commissionAmount) / numberOfInstallments;
        LocalDate startDate = LocalDate.now();

        return IntStream.range(0, numberOfInstallments)
                .mapToObj(i -> {
                    LocalDate scheduledDate = startDate.plusDays(15L * (i+1));
                    return createInstallment(paymentPerInstallment, scheduledDate);
                })
                .collect(Collectors.toList());
    }

    private Installment createInstallment(double paymentPerInstallment, LocalDate scheduledDate) {
        return Installment.builder()
                .amount(paymentPerInstallment)
                .scheduledPaymentDate(scheduledDate)
                .status(InstallmentStatus.NEXT)
                .build();
    }
}
