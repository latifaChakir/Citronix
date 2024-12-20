package com.example.citronix.domain.vm.wrapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private T data;
    private String message;
    private int statusCode;
    private LocalDateTime timestamp;
    private String path;
    private boolean success;
    private String traceId;

    // Méthode pour répondre avec une réussite
    public static <T> ApiResponse<T> success(T data, String path) {
        return ApiResponse.<T>builder()
                .data(data)
                .message("Opération réussie")
                .statusCode(200)
                .timestamp(LocalDateTime.now())
                .path(path)
                .success(true)
                .traceId(generateTraceId())
                .build();
    }

    // Méthode pour répondre avec une erreur
    public static <T> ApiResponse<T> error(String message, String path, int statusCode) {
        return ApiResponse.<T>builder()
                .message(message)
                .statusCode(statusCode)
                .timestamp(LocalDateTime.now())
                .path(path)
                .success(false)
                .traceId(generateTraceId())
                .build();
    }

    // Génération d'un traceId unique
    private static String generateTraceId() {
        return "trace-" + LocalDateTime.now().toString();
    }
}
