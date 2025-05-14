package com.aplazo.mx.creditlines.controller.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class CustomerResponse {
    private UUID idClient;
    private LocalDate creationDate;
    private Double creditLineAmount;
    private Double availableCreditLineAmount;
}
