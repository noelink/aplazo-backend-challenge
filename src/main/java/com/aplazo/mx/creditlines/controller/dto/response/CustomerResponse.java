package com.aplazo.mx.creditlines.controller.dto.response;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class CustomerResponse {
    private UUID idCliente;
    private LocalDate creationDate;
    private Double creditLineAmount;
    private Double availableCreditLineAmount;
}
