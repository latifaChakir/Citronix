package com.example.citronix.shared.exception;

public class FarmAlreadyExistsException extends RuntimeException {
    public FarmAlreadyExistsException(String message) {
        super(message);
    }
}
