package com.aplazo.mx.creditlines.repository.entity;

import com.aplazo.mx.creditlines.enums.InstallmentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "installment")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Installment {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "scheduled_payment_date")
    private LocalDate scheduledPaymentDate;

    @Enumerated(EnumType.STRING)
    private InstallmentStatus status;
}