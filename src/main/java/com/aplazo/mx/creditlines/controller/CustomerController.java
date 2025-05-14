package com.aplazo.mx.creditlines.controller;


import com.aplazo.mx.creditlines.controller.dto.request.CustomerRequest;
import com.aplazo.mx.creditlines.controller.dto.response.CustomerResponse;
import com.aplazo.mx.creditlines.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value="aplazo-api-backend/v1/customers", produces = "application/json")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<CustomerResponse> createCustomer(@RequestBody @Valid CustomerRequest request) {
//        CustomerResponse response = new CustomerResponse();
//
        HttpHeaders headers = new HttpHeaders();
//        headers.setLocation(URI.create("/v1/customers/" + response.getIdCliente()));
//        headers.add("X-Auth-Token", "fake-jwt-token");
        return ResponseEntity.status(HttpStatus.CREATED).headers(headers).body(customerService.createCustomer(request));
    }

}
