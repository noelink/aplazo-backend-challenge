package com.aplazo.mx.creditlines.controller.dto.response;

import com.aplazo.mx.creditlines.controller.dto.PaymentPlanDto;
import com.aplazo.mx.creditlines.enums.PaymentPlanStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanResponse {
    private UUID id;
    private UUID customerId;
    private PaymentPlanStatus status;
    private Instant createdAt;
    private PaymentPlanDto paymentPlan;
}
