package com.aplazo.mx.creditlines.repository.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "payment_plan")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentPlan {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "commission_amount")
    private Double commissionAmount;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_plan_id")
    private List<Installment> installments;
}