package com.aplazo.mx.creditlines.service;

import com.aplazo.mx.creditlines.business.CreditLineCalculatorStrategy;
import com.aplazo.mx.creditlines.controller.dto.InstallmentDto;
import com.aplazo.mx.creditlines.controller.dto.PaymentPlanDto;
import com.aplazo.mx.creditlines.controller.dto.request.CustomerRequest;
import com.aplazo.mx.creditlines.controller.dto.response.CustomerResponse;
import com.aplazo.mx.creditlines.controller.dto.response.LoanResponse;
import com.aplazo.mx.creditlines.enums.InstallmentStatus;
import com.aplazo.mx.creditlines.enums.PaymentPlanStatus;
import com.aplazo.mx.creditlines.exception.CustomerCreditException;
import com.aplazo.mx.creditlines.repository.CustomerRepository;
import com.aplazo.mx.creditlines.repository.entity.Customer;
import com.aplazo.mx.creditlines.repository.entity.Loan;
import com.aplazo.mx.creditlines.service.impl.CustomerServiceImpl;
import com.aplazo.mx.creditlines.util.mapper.CustomerMapper;
import com.aplazo.mx.creditlines.util.mapper.LoanMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;
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
    CustomerMapper customerMapper;

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

        InstallmentDto installment1 = new InstallmentDto();
        installment1.setAmount(1000.0);
        installment1.setScheduledPaymentDate(LocalDate.of(2025, 6, 15));
        installment1.setStatus(InstallmentStatus.NEXT);

        InstallmentDto installment2 = new InstallmentDto();
        installment2.setAmount(1000.0);
        installment2.setScheduledPaymentDate(LocalDate.of(2025, 7, 15));
        installment2.setStatus(InstallmentStatus.PENDING);

        // Crear el PaymentPlanDto
        PaymentPlanDto paymentPlanDto = new PaymentPlanDto();
        paymentPlanDto.setCommissionAmount(150.0);
        paymentPlanDto.setInstallments(Arrays.asList(installment1, installment2));

        // Crear el LoanResponse
        LoanResponse loanResponse = new LoanResponse();
        loanResponse.setId(UUID.randomUUID());
        loanResponse.setCustomerId(UUID.randomUUID());
        loanResponse.setStatus(PaymentPlanStatus.ACTIVE);
        loanResponse.setCreatedAt(Instant.now());
        loanResponse.setPaymentPlan(paymentPlanDto);
    }

    @Test
    void testCreateCustomer_Success() {


        when(customerMapper.toCustomer(any(CustomerRequest.class))).thenReturn(customer);

        when(creditLineCalculatorStrategy.calculateCreditLine(anyInt()))
                .thenReturn(Optional.of(5000.00));


        when(customerRepository.save(any(Customer.class)))
                .thenReturn(customer);

        CustomerResponse result = customerService.createCustomer(customerRequest);

        assertNotNull(result);
        assertEquals(customer.getIdClient(), result.getIdClient());
        assertEquals(5000.00, result.getCreditLineAmount(), 0.001);
        assertEquals(5000.00, result.getAvailableCreditLineAmount(), 0.001);
    }

    @Test
    void testCreateCustomer_Failure_AgeNotAllowed() {

        when(customerMapper.toCustomer(any(CustomerRequest.class))).thenReturn(customer);

        when(creditLineCalculatorStrategy.calculateCreditLine(anyInt()))
                .thenReturn(Optional.empty());

        assertThrows(CustomerCreditException.class, () -> {
            customerService.createCustomer(customerRequest);
        });
    }

    @Test
    void testFindByCustomerId_Success() {

        when(customerMapper.toCustomerResponse(any(Customer.class))).thenReturn(customerResponse);
        when(customerRepository.findById(any(UUID.class)))
                .thenReturn(Optional.of(customer));

        CustomerResponse result = customerService.findByCustomerId(customer.getIdClient());


        assertNotNull(result);
        assertEquals(customer.getIdClient(), result.getIdClient());
    }

    @Test
    void testFindByCustomerId_NotFound() {
        when(customerMapper.toCustomerResponse(any(Customer.class))).thenReturn(customerResponse);
        when(customerRepository.findById(any(UUID.class)))
                .thenReturn(Optional.empty());

        assertThrows(CustomerCreditException.class, () -> {
            customerService.findByCustomerId(UUID.randomUUID());
        });
    }
}
