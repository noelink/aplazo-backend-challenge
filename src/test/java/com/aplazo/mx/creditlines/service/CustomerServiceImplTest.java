package com.aplazo.mx.creditlines.service;

import com.aplazo.mx.creditlines.business.CreditLineCalculatorStrategy;
import com.aplazo.mx.creditlines.controller.dto.request.CustomerRequest;
import com.aplazo.mx.creditlines.controller.dto.response.CustomerResponse;
import com.aplazo.mx.creditlines.exception.CustomerCreditException;
import com.aplazo.mx.creditlines.repository.CustomerRepository;
import com.aplazo.mx.creditlines.repository.entity.Customer;
import com.aplazo.mx.creditlines.service.impl.CustomerServiceImpl;
import com.aplazo.mx.creditlines.util.mapper.CustomerMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CreditLineCalculatorStrategy creditLineCalculatorStrategy;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private CustomerRequest customerRequest;
    private Customer customer;
    private CustomerResponse customerResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        customerRequest = new CustomerRequest();
        customerRequest.setFirstName("Fernando");
        customerRequest.setLastName("Navarro");
        customerRequest.setSecondLastName("Arciniega");
        customerRequest.setDateOfBirth("1995-05-20");

        customer = new Customer();
        customer.setIdClient(UUID.randomUUID());
        customer.setCreditLineAmount(5000.00);

        customerResponse = CustomerResponse.builder()
                .idClient(customer.getIdClient())
                .creationDate(LocalDate.now())
                .creditLineAmount(customer.getCreditLineAmount())
                .availableCreditLineAmount(customer.getCreditLineAmount())
                .build();
    }

//    @Test
//    void testCreateCustomer_Success() {
//
//        when(creditLineCalculatorStrategy.calculateCreditLine(anyInt()))
//                .thenReturn(Optional.of(5000.00));
//
//
//        when(customerRepository.save(any(Customer.class)))
//                .thenReturn(customer);
//
//        CustomerResponse result = customerService.createCustomer(customerRequest);
//
//        assertNotNull(result);
//        assertEquals(customer.getIdClient(), result.getIdClient());
//        assertEquals(5000.00, result.getCreditLineAmount(), 0.001);
//        assertEquals(5000.00, result.getAvailableCreditLineAmount(), 0.001);
//    }

    @Test
    void testCreateCustomer_Failure_AgeNotAllowed() {

        when(creditLineCalculatorStrategy.calculateCreditLine(anyInt()))
                .thenReturn(Optional.empty());

        assertThrows(CustomerCreditException.class, () -> {
            customerService.createCustomer(customerRequest);
        });
    }

//    @Test
//    void testFindByCustomerId_Success() {
//
//        when(customerRepository.findById(any(UUID.class)))
//                .thenReturn(Optional.of(customer));
//
//        CustomerResponse result = customerService.findByCustomerId(customer.getIdClient());
//
//
//        assertNotNull(result);
//        assertEquals(customer.getIdClient(), result.getIdClient());
//    }

    @Test
    void testFindByCustomerId_NotFound() {

        when(customerRepository.findById(any(UUID.class)))
                .thenReturn(Optional.empty());

        assertThrows(CustomerCreditException.class, () -> {
            customerService.findByCustomerId(UUID.randomUUID());
        });
    }
}
