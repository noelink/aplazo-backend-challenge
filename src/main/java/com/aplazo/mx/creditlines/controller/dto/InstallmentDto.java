package com.aplazo.mx.creditlines.controller.dto;

import com.aplazo.mx.creditlines.enums.InstallmentStatus;
import lombok.*;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InstallmentDto {
    private double amount;
    private LocalDate scheduledPaymentDate;
    private InstallmentStatus status;
}
