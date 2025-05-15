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

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CreditLineCalculatorStrategy creditLineCalculatorStrategy;
    private final CustomerMapper customerMapper;

    @Override
    public CustomerResponse createCustomer(CustomerRequest customerRequest) {
        Optional<Double> creditLineAmountOption = creditLineCalculatorStrategy.calculateCreditLine(DateCalculator.calcularEdad(customerRequest.getDateOfBirth()));
        if(creditLineAmountOption.isEmpty()){
            throw new CustomerCreditException("Rejected customer request, age not allowed", 100);
        }
        log.info("Creating new custumer {}", customerRequest);
        Customer customer = customerMapper.toCustomer(customerRequest);
        customer.setCreditLineAmount(creditLineAmountOption.get());
        Customer savedCustomer = customerRepository.save(customer);
        return CustomerResponse.builder()
                .idClient(savedCustomer.getIdClient())
                .creationDate(LocalDate.now())
                .creditLineAmount(creditLineAmountOption.get())
                .availableCreditLineAmount(creditLineAmountOption.get())
                .build();

    }

    @Override
    public CustomerResponse findByCustomerId(UUID uuid) {
        Optional<Customer> customerOption = customerRepository.findById(uuid);
        if(customerOption.isEmpty()) {
            throw new CustomerCreditException("No such customer found with id " + uuid);
        }
        log.info("Retieve customer with id {}", uuid);
        CustomerResponse response = customerMapper.toCustomerResponse(customerOption.get());
        response.setAvailableCreditLineAmount(customerOption.get().getCreditLineAmount());
        return response;
    }
}
