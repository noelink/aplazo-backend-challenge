package com.aplazo.mx.creditlines.controller;

import com.aplazo.mx.creditlines.controller.dto.request.CustomerRequest;
import com.aplazo.mx.creditlines.controller.dto.response.CustomerResponse;
import com.aplazo.mx.creditlines.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CustomerService customerService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateCustomerSuccess() throws Exception {
        CustomerRequest request = new CustomerRequest();
        request.setFirstName("Juan");
        request.setLastName("Pérez");
        request.setSecondLastName("López");
        request.setDateOfBirth("1990-05-10");

        UUID id = UUID.randomUUID();
        CustomerResponse response = CustomerResponse.builder()
                .idClient(id)
                .creationDate(LocalDate.now())
                .creditLineAmount(8000.0)
                .availableCreditLineAmount(8000.0)
                .build();

        Mockito.when(customerService.createCustomer(any(CustomerRequest.class))).thenReturn(response);

        mockMvc.perform(post("/aplazo-api-backend/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idClient").value(id.toString()))
                .andExpect(jsonPath("$.creditLineAmount").value(8000.0));
    }

    @Test
    void testCreateCustomerInvalidRequest() throws Exception {
        CustomerRequest request = new CustomerRequest();
        request.setFirstName("");
        request.setLastName("");
        request.setSecondLastName("López");
        request.setDateOfBirth("19900510");

        mockMvc.perform(post("/aplazo-api-backend/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnCustomerWhenIdIsValid() throws Exception {

        UUID customerId = UUID.randomUUID();
        CustomerResponse response = CustomerResponse.builder()
                .idClient(customerId)
                .creationDate(LocalDate.now())
                .creditLineAmount(5000.0)
                .availableCreditLineAmount(3000.0)
                .build();

        Mockito.when(customerService.findByCustomerId(customerId))
                .thenReturn(response);

        mockMvc.perform(get("/aplazo-api-backend/v1/customers/" + customerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idClient").value(customerId.toString()))
                .andExpect(jsonPath("$.creditLineAmount").value(5000.0))
                .andExpect(jsonPath("$.availableCreditLineAmount").value(3000.0));
    }
}