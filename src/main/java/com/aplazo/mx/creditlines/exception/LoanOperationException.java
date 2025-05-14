package com.aplazo.mx.creditlines.exception;

public class LoanOperationException extends RuntimeException {

    private int errorCode;

    public LoanOperationException(String message) {
        super(message);
    }

    public LoanOperationException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
