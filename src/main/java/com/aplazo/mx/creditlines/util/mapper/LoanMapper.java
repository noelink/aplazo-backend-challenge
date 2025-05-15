package com.aplazo.mx.creditlines.util.mapper;

import com.aplazo.mx.creditlines.controller.dto.response.LoanResponse;
import com.aplazo.mx.creditlines.repository.entity.Loan;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface LoanMapper {

    LoanResponse loanToLoanResponse(Loan loan);
}
