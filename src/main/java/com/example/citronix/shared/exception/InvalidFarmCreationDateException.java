package com.example.citronix.shared.exception;

public class InvalidFarmCreationDateException extends RuntimeException {
    public InvalidFarmCreationDateException(String message) {
        super(message);
    }
}
