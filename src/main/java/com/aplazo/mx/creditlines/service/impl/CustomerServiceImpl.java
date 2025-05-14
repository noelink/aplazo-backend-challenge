package com.aplazo.mx.creditlines.service.impl;

import com.aplazo.mx.creditlines.business.CreditLineCalculatorStrategy;
import com.aplazo.mx.creditlines.controller.dto.request.CustomerRequest;
import com.aplazo.mx.creditlines.controller.dto.response.CustomerResponse;
import com.aplazo.mx.creditlines.exception.CustomerCreditException;
import com.aplazo.mx.creditlines.repository.CustomerRepository;
import com.aplazo.mx.creditlines.repository.entity.Customer;
import com.aplazo.mx.creditlines.service.CustomerService;
import com.aplazo.mx.creditlines.util.DateCalculator;
import com.aplazo.mx.creditlines.util.mapper.CustomerMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CreditLineCalculatorStrategy creditLineCalculatorStrategy;

    @Override
    public CustomerResponse createCustomer(CustomerRequest customerRequest) {
        Optional<Double> creditLineAmountOption = creditLineCalculatorStrategy.calculateCreditLine(DateCalculator.calcularEdad(customerRequest.getDateOfBirth()));
        if(creditLineAmountOption.isEmpty()){
            throw new CustomerCreditException("Rejected customer request, age not allowed", 100);
        }
        log.info("Creating new custumer {}", customerRequest);
        Customer customer = CustomerMapper.INSTANCE.toCustomer(customerRequest);
        customer.setCreditLineAmount(creditLineAmountOption.get());
        Customer savedCustomer = customerRepository.save(customer);
        return CustomerResponse.builder()
                .idCliente(savedCustomer.getIdClient())
                .createdAt(LocalDateTime.now())
                .creditLineAmount(creditLineAmountOption.get())
                .availableCreditLineAmount(creditLineAmountOption.get())
                .build();

    }
}
