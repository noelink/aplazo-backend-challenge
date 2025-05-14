package com.aplazo.mx.creditlines.exception;

import com.aplazo.mx.creditlines.util.CreditConstants;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
@Slf4j
public class ErrorHandler extends ResponseEntityExceptionHandler {

    private static final String HTTP_MESSAGE_NOT_READABLE_ERROR_MESSAGE = "The request cannot be fulfilled due to bad syntax. ";
    private static final String CHECK_ERROR_MESSAGE = "Check the exception error message.";

    @ExceptionHandler(CustomerCreditException.class)
    public ResponseEntity<ErrorResponse> handleCustomerCreditException(CustomerCreditException ce, WebRequest request, HttpServletRequest servletRequest) {
        log.error("An error occurred: {}", ce.getMessage());
        String path = servletRequest.getRequestURI();
        ErrorResponse errorResponse = prepareErrorResponse(ce.getErrorCode(), ce.getMessage(), path);

        return new ResponseEntity<>(errorResponse, errorResponse.getHttpCode());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalUUID(IllegalArgumentException ex, WebRequest request, HttpServletRequest servletRequest) {
        log.error("Error parsing the UUID: {}", ex.getMessage());
        String path = servletRequest.getRequestURI();
        ErrorResponse errorResponse = prepareErrorResponse(103, ex.getMessage(), path);
        return new ResponseEntity<>(errorResponse, errorResponse.getHttpCode());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.error(HTTP_MESSAGE_NOT_READABLE_ERROR_MESSAGE + CHECK_ERROR_MESSAGE, ex);
        String path = request.getContextPath();
        ErrorResponse errorResponse = new ErrorResponse(CreditConstants.ERR_APZ_02,
                CreditConstants.MSG_APZ_02,
                ex.getMessage(),
                path,
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    private ErrorResponse prepareErrorResponse(int errorCode, String errorMessage, String path) {
        return switch (errorCode) {
            case 100 -> new ErrorResponse(
                    CreditConstants.ERR_APZ_01,
                    HttpStatus.INTERNAL_SERVER_ERROR.name(),
                    errorMessage,
                    path,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    LocalDateTime.now()
            );
            case 101, 102 -> new ErrorResponse(
                    CreditConstants.ERR_APZ_03,
                    CreditConstants.MSG_APZ_03,
                    errorMessage,
                    path,
                    HttpStatus.TOO_MANY_REQUESTS,
                    LocalDateTime.now()
            );
            case 103 -> new ErrorResponse(
                    CreditConstants.ERR_APZ_04,
                    CreditConstants.MSG_APZ_04,
                    errorMessage,
                    path,
                    HttpStatus.BAD_REQUEST,
                    LocalDateTime.now()
            );
            case 104 -> new ErrorResponse(
                    CreditConstants.ERR_APZ_07,
                    CreditConstants.MSG_APZ_07,
                    errorMessage,
                    path,
                    HttpStatus.UNAUTHORIZED,
                    LocalDateTime.now()
            );
            case 105 -> new ErrorResponse(
                    CreditConstants.ERR_APZ_05,
                    CreditConstants.MSG_APZ_05,
                    errorMessage,
                    path,
                    HttpStatus.NOT_FOUND,
                    LocalDateTime.now()
            );
            default -> throw new IllegalArgumentException("Código de error no soportado: " + errorCode);
        };
    }


}
