package com.aplazo.mx.creditlines.controller.dto.response;


import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class CustomerResponse {
    private UUID idCliente;
    private LocalDateTime createdAt;
    private BigDecimal creditLineAmount;
    private BigDecimal availableCreditLineAmount;
}
