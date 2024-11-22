package com.example.citronix.shared.exception;

public class HarvestNotFoundException extends RuntimeException {
    public HarvestNotFoundException(String message) {
        super(message);
    }
}
