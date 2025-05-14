package com.aplazo.mx.creditlines.controller;

import com.aplazo.mx.creditlines.controller.dto.PaymentPlanDto;
import com.aplazo.mx.creditlines.controller.dto.request.LoanRequest;
import com.aplazo.mx.creditlines.controller.dto.response.LoanResponse;
import com.aplazo.mx.creditlines.enums.PaymentPlanStatus;
import com.aplazo.mx.creditlines.service.LoanService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LoanController.class)
class LoanControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LoanService loanService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateLoan() throws Exception {
        LoanRequest request = new LoanRequest();
        request.setCustomerId("12345");
        request.setAmount(5000.0);

        UUID loanId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();

        LoanResponse mockResponse = new LoanResponse();
        mockResponse.setId(loanId);
        mockResponse.setCustomerId(customerId);
        mockResponse.setStatus(PaymentPlanStatus.PENDING);
        mockResponse.setCreatedAt(Instant.now());
        mockResponse.setPaymentPlan(new PaymentPlanDto());

        Mockito.when(loanService.createLoan(any(LoanRequest.class))).thenReturn(mockResponse);

        mockMvc.perform(post("/aplazo-api-backend/v1/loans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(loanId.toString()))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    void testGetLoan() throws Exception {
        UUID loanId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();

        LoanResponse mockResponse = new LoanResponse();
        mockResponse.setId(loanId);
        mockResponse.setCustomerId(customerId);
        mockResponse.setStatus(PaymentPlanStatus.ACTIVE);
        mockResponse.setCreatedAt(Instant.now());
        mockResponse.setPaymentPlan(new PaymentPlanDto());

        Mockito.when(loanService.findLoanById(eq(loanId))).thenReturn(mockResponse);

        mockMvc.perform(get("/aplazo-api-backend/v1/loans/{loanId}", loanId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(loanId.toString()))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }
}
