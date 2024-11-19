package com.example.citronix.api.advice;

import com.example.citronix.domain.vm.wrapper.ApiResponse;
import com.example.citronix.shared.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(FarmNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleFarmNotFoundException(FarmNotFoundException ex) {
        ApiResponse<String> response = ApiResponse.error(
                ex.getMessage(),
                "/api/farms",
                HttpStatus.NOT_FOUND.value()
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleGenericException(Exception ex) {
        ApiResponse<String> response = ApiResponse.error("Une erreur interne est survenue", "/api/generic-error", HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        ApiResponse<Map<String, String>> response = ApiResponse.error("Validation failed", "/api/validation-error", HttpStatus.BAD_REQUEST.value());
        response.setData(errors);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(FarmAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<String>> handleFarmAlreadyExistsException(FarmAlreadyExistsException ex) {
        ApiResponse<String> response = ApiResponse.error(
                ex.getMessage(),
                "/api/farms",
                HttpStatus.CONFLICT.value()
        );
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }
    @ExceptionHandler(InvalidFarmCreationDateException.class)
    public ResponseEntity<ApiResponse<String>> handleInvalidFarmCreationDateException(InvalidFarmCreationDateException ex) {
        ApiResponse<String> response = ApiResponse.error(
                ex.getMessage(),
                "/api/farms",
                HttpStatus.BAD_REQUEST.value()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(TotalFieldAreaExceedsFarmAreaException.class)
    public ResponseEntity<ApiResponse<String>> handleTotalFieldAreaExceedsFarmAreaException(TotalFieldAreaExceedsFarmAreaException ex) {
        ApiResponse<String> response = ApiResponse.error(
                ex.getMessage(),
                "/api/farms",
                HttpStatus.BAD_REQUEST.value()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidFieldAreaException.class)
    public ResponseEntity<ApiResponse<String>> handleInvalidFieldAreaException(InvalidFieldAreaException ex) {
        ApiResponse<String> response = ApiResponse.error(
                ex.getMessage(),
                "/api/fields",
                HttpStatus.BAD_REQUEST.value()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
