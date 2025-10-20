package com.app.geoTracker.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiError {
    private int status;
    private String error;
    private String message;
    private LocalDateTime timestamp = LocalDateTime.now();
    private Map<String, String> details;

    // ✅ Custom constructor for quick use
    public ApiError(HttpStatus status, String message) {
        this.status = status.value();
        this.error = status.getReasonPhrase();
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    // ✅ Another simple version without details
    public ApiError(String message, int status, LocalDateTime timestamp) {
        this.message = message;
        this.status = status;
        this.timestamp = timestamp;
    }

    // ✅ NEW constructor to handle field validation errors
    public ApiError(String message, int status, LocalDateTime timestamp, Map<String, String> details) {
        this.message = message;
        this.status = status;
        this.timestamp = timestamp;
        this.details = details;
    }
}

