package com.aplazo.mx.creditlines.exception;

import com.aplazo.mx.creditlines.util.CreditConstants;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@ControllerAdvice
@Slf4j
public class ErrorHandler {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(CustomerCreditException.class)
    public ResponseEntity<ErrorResponse> handleCustomerCreditException(CustomerCreditException ce, WebRequest request, HttpServletRequest servletRequest) {
        log.error("An error occurred: {}", ce.getMessage());
        String path = servletRequest.getRequestURI();

        ErrorResponse errorResponse = new ErrorResponse(CreditConstants.ERR_APZ_01,
                HttpStatus.INTERNAL_SERVER_ERROR.name(),
                ce.getMessage(),
                path,
                LocalDateTime.now()  );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(LoanOperationException.class)
    public ResponseEntity<ErrorResponse> handleCustomerCreditException(LoanOperationException le, WebRequest request, HttpServletRequest servletRequest) {
        log.error("An error occurred: {}", le.getMessage());
        String path = servletRequest.getRequestURI();

        ErrorResponse errorResponse = new ErrorResponse(CreditConstants.ERR_APZ_01,
                HttpStatus.INTERNAL_SERVER_ERROR.name(),
                le.getMessage(),
                path,
                LocalDateTime.now()  );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
