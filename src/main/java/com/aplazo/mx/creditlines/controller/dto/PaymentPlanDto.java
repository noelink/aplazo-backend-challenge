package com.aplazo.mx.creditlines.controller.dto;

import lombok.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentPlanDto {
    private double commissionAmount;
    private List<InstallmentDto> installments;
}