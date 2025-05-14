package com.aplazo.mx.creditlines.service;

import com.aplazo.mx.creditlines.controller.dto.request.LoanRequest;
import com.aplazo.mx.creditlines.controller.dto.response.LoanResponse;

import java.util.UUID;

public interface LoanService {

    public LoanResponse createLoan(LoanRequest loanRequest);

    public LoanResponse findLoanById(UUID loanId);
}
