package com.example.citronix.shared.exception;

public class SaleNotFoundException extends RuntimeException {
    public SaleNotFoundException(String message) {
        super(message);
    }
}
