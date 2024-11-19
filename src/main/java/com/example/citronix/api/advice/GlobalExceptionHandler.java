package com.example.citronix.api.advice;

import com.example.citronix.domain.vm.wrapper.ApiResponse;
import com.example.citronix.shared.exception.FarmNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(FarmNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleFarmNotFoundException(FarmNotFoundException ex) {
        ApiResponse<String> response = ApiResponse.error(ex.getMessage(), "/api/farms", HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleGenericException(Exception ex) {
        ApiResponse<String> response = ApiResponse.error("Une erreur interne est survenue", "/api/generic-error", HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
