package com.aplazo.mx.creditlines.repository.entity;

import com.aplazo.mx.creditlines.enums.PaymentPlanStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.*;

@Entity
@Table(name = "loan")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Loan {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "customer_id")
    private UUID customerId;

    @Column(name = "loan_amount")
    private Double loanAmount;

    @Enumerated(EnumType.STRING)
    private PaymentPlanStatus status;

    @Column(name = "created_at")
    private Instant createdAt;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_plan_id")
    private PaymentPlan paymentPlan;
}
