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
            log.error("No such customer found with id {}", loanRequest.getCustomerId());
            throw new CustomerCreditException("No such customer found with id " + loanRequest.getCustomerId(), 105);
        }
        Customer customer = customerOption.get();
        if (loanRequest.getAmount().compareTo(customer.getCreditLineAmount()) > 0) {
            log.info("Invalid amount of credit line");
            throw new CustomerCreditException("Requested amount: "+loanRequest.getAmount()+ " exceeds credit limit of: " + customer.getCreditLineAmount(), 102);
        }
        Loan newLoan = new Loan();

        newLoan.setLoanAmount(loanRequest.getAmount());
        newLoan.setCustomerId(customer.getIdClient());
        newLoan.setStatus(PaymentPlanStatus.ACTIVE);
        newLoan.setCreatedAt(Instant.now());
        newLoan.setPaymentPlan(paymentPlanCalculator.createPaymentPlan(customer, NUMBER_OF_INSTALLMENTS, loanRequest.getAmount()));
        return LoanMapper.INSTANCE.loanToLoanResponse(loanRepository.save(newLoan));
    }

    @Override
    public LoanResponse findLoanById(UUID loanId) {
        Optional<Loan> loanOption = loanRepository.findById(loanId);
        if(loanOption.isEmpty()) {
            throw new CustomerCreditException("No such loan found with id " + loanId);
        }
        log.info("Retieve loan with id {}", loanId);
       return LoanMapper.INSTANCE.loanToLoanResponse(loanOption.get());
    }


}
