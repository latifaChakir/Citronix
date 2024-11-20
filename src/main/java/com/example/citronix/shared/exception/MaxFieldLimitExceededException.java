package com.example.citronix.shared.exception;

public class MaxFieldLimitExceededException extends RuntimeException {
    public MaxFieldLimitExceededException(String message) {
        super(message);
    }
}
