package com.aplazo.mx.creditlines.service.impl;

import com.aplazo.mx.creditlines.business.PaymentPlanCalculator;
import com.aplazo.mx.creditlines.controller.dto.request.LoanRequest;
import com.aplazo.mx.creditlines.controller.dto.response.LoanResponse;
import com.aplazo.mx.creditlines.enums.PaymentPlanStatus;
import com.aplazo.mx.creditlines.exception.CustomerCreditException;
import com.aplazo.mx.creditlines.repository.CustomerRepository;
import com.aplazo.mx.creditlines.repository.LoanRepository;
import com.aplazo.mx.creditlines.repository.entity.Customer;
import com.aplazo.mx.creditlines.repository.entity.Loan;
import com.aplazo.mx.creditlines.service.LoanService;
import com.aplazo.mx.creditlines.util.mapper.LoanMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class LoanServiceImpl implements LoanService {

    private final LoanRepository loanRepository;
    private final CustomerRepository customerRepository;
    private final PaymentPlanCalculator paymentPlanCalculator;
    private final int NUMBER_OF_INSTALLMENTS = 5;

    @Override
    public LoanResponse createLoan(LoanRequest loanRequest) {
        Optional<Customer> customerOption = customerRepository.findById(UUID.fromString(loanRequest.getCustomerId()));
        if(customerOption.isEmpty()) {
            throw new CustomerCreditException("No such customer found with id " + loanRequest.getCustomerId());
        }
        Customer customer = customerOption.get();
        if (loanRequest.getAmount().compareTo(customer.getCreditLineAmount()) > 0) {
            throw new CustomerCreditException("Requested amount exceeds credit limit " + loanRequest.getAmount());
        }
        Loan newLoan = new Loan();

        newLoan.setLoanAmount(loanRequest.getAmount());
        newLoan.setCustomerId(customer.getIdClient());
        newLoan.setStatus(PaymentPlanStatus.ACTIVE);
        newLoan.setCreatedAt(Instant.now());
        newLoan.setPaymentPlan(paymentPlanCalculator.createPaymentPlan(customer, NUMBER_OF_INSTALLMENTS, loanRequest.getAmount()));
        return LoanMapper.INSTANCE.loanToLoanResponse(loanRepository.save(newLoan));
    }



}
