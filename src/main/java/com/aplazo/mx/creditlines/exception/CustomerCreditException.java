package com.aplazo.mx.creditlines.exception;

public class CustomerCreditException extends RuntimeException {

    private int errorCode;

    public CustomerCreditException(String message) {
       super(message);
    }

    public CustomerCreditException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

}
