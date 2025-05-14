package com.aplazo.mx.creditlines.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class LoanRequest {

    @NotBlank
    private String customerId;
    @NotNull
    private Double amount;
}
