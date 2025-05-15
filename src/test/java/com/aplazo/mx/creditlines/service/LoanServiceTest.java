package com.aplazo.mx.creditlines.service;

import com.aplazo.mx.creditlines.business.PaymentPlanCalculator;
import com.aplazo.mx.creditlines.controller.dto.InstallmentDto;
import com.aplazo.mx.creditlines.controller.dto.PaymentPlanDto;
import com.aplazo.mx.creditlines.controller.dto.request.LoanRequest;
import com.aplazo.mx.creditlines.controller.dto.response.LoanResponse;
import com.aplazo.mx.creditlines.enums.InstallmentStatus;
import com.aplazo.mx.creditlines.enums.PaymentPlanStatus;
import com.aplazo.mx.creditlines.exception.CustomerCreditException;
import com.aplazo.mx.creditlines.repository.CustomerRepository;
import com.aplazo.mx.creditlines.repository.LoanRepository;
import com.aplazo.mx.creditlines.repository.entity.Customer;
import com.aplazo.mx.creditlines.repository.entity.Loan;
import com.aplazo.mx.creditlines.repository.entity.PaymentPlan;
import com.aplazo.mx.creditlines.service.impl.LoanServiceImpl;
import com.aplazo.mx.creditlines.util.mapper.LoanMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LoanServiceTest {

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private LoanMapper loanMapper;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private PaymentPlanCalculator paymentPlanCalculator;

    @InjectMocks
    private LoanServiceImpl loanService;

    private final UUID customerId = UUID.fromString("2be415cd-bfc0-4175-b2b8-e00ac75793c1");
    private final UUID loanId = UUID.fromString("2be415cd-bfc0-4175-b2b8-e00ac75793c1");

    private LoanRequest loanRequest;
    private Customer customer;
    private Loan loan;
    private PaymentPlan paymentPlan;
    private LoanResponse loanResponse;

    @BeforeEach
    void setUp() {

        InstallmentDto installment1 = new InstallmentDto();
        installment1.setAmount(1000.0);
        installment1.setScheduledPaymentDate(LocalDate.of(2025, 6, 15));
        installment1.setStatus(InstallmentStatus.NEXT);

        InstallmentDto installment2 = new InstallmentDto();
        installment2.setAmount(1000.0);
        installment2.setScheduledPaymentDate(LocalDate.of(2025, 7, 15));
        installment2.setStatus(InstallmentStatus.PENDING);

        // Plan de pagos
        PaymentPlanDto paymentPlanDto = new PaymentPlanDto();
        paymentPlanDto.setCommissionAmount(150.0);
        paymentPlanDto.setInstallments(Arrays.asList(installment1, installment2));

        // LoanResponse
        loanResponse = new LoanResponse();
        loanResponse.setId(UUID.fromString("2be415cd-bfc0-4175-b2b8-e00ac75793c1"));
        loanResponse.setCustomerId(UUID.fromString("2be415cd-bfc0-4175-b2b8-e00ac75793c1"));
        loanResponse.setStatus(PaymentPlanStatus.ACTIVE);
        loanResponse.setCreatedAt(Instant.now());
        loanResponse.setPaymentPlan(paymentPlanDto);

        loanRequest = new LoanRequest();
        loanRequest.setCustomerId(customerId.toString());
        loanRequest.setAmount(5000.0);

        customer = new Customer();
        customer.setIdClient(customerId);
        customer.setCreditLineAmount(8000.0);

        paymentPlan = new PaymentPlan();
        paymentPlan.setCommissionAmount(100.0);
        paymentPlan.setInstallments(Collections.emptyList());

        loan = new Loan();
        loan.setId(loanId);
        loan.setCustomerId(customerId);
        loan.setLoanAmount(loanRequest.getAmount());
        loan.setStatus(PaymentPlanStatus.ACTIVE);
        loan.setCreatedAt(Instant.now());
        loan.setPaymentPlan(paymentPlan);
    }

    @Test
    void createLoan_success() {
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(paymentPlanCalculator.createPaymentPlan(customer, 5, 5000.0)).thenReturn(paymentPlan);
        when(loanMapper.loanToLoanResponse(any(Loan.class))).thenReturn(loanResponse);
        when(loanRepository.save(any(Loan.class))).thenReturn(loan);

        LoanResponse response = loanService.createLoan(loanRequest);

        assertNotNull(response);
        assertEquals(loanId, response.getId());
        assertEquals(PaymentPlanStatus.ACTIVE, response.getStatus());
        verify(loanRepository).save(any(Loan.class));
    }

    @Test
    void createLoan_customerNotFound_throwsException() {
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        CustomerCreditException exception = assertThrows(CustomerCreditException.class, () -> loanService.createLoan(loanRequest));
        assertEquals("No such customer found with id " + loanRequest.getCustomerId(), exception.getMessage());
        assertEquals(105, exception.getErrorCode());
    }

    @Test
    void createLoan_exceedsCreditLimit_throwsException() {
        customer.setCreditLineAmount(1000.0);
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        CustomerCreditException exception = assertThrows(CustomerCreditException.class, () -> loanService.createLoan(loanRequest));
        assertTrue(exception.getMessage().contains("exceeds credit limit"));
        assertEquals(102, exception.getErrorCode());
    }

    @Test
    void findLoanById_success() {
        when(loanMapper.loanToLoanResponse(loan)).thenReturn(loanResponse);
        when(loanRepository.findById(loanId)).thenReturn(Optional.of(loan));

        LoanResponse response = loanService.findLoanById(loanId);

        assertNotNull(response);
        assertEquals(loanId, response.getId());
        assertEquals(customerId, response.getCustomerId());
        assertEquals(PaymentPlanStatus.ACTIVE, response.getStatus());
    }

    @Test
    void findLoanById_notFound_throwsException() {
        when(loanRepository.findById(loanId)).thenReturn(Optional.empty());

        CustomerCreditException exception = assertThrows(CustomerCreditException.class, () -> loanService.findLoanById(loanId));
        assertEquals("No such loan found with id " + loanId, exception.getMessage());
    }
}
