package com.yt.backend.subscription.globalExceptions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.yt.backend.subscription.exceptions.SubscriptionException;

@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("status", "error");
        error.put("message", ex.getMessage());
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleException(Exception ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("status", "error");
        error.put("message", "An unexpected error occurred: " + ex.getMessage());
        return ResponseEntity.status(500).body(error);
    }

    @ExceptionHandler(SubscriptionException.class)
    public ResponseEntity<?> handleSubscriptionException(SubscriptionException ex) {

        Map<String, Object> response = Map.of(
                "status", "error",
                "message", ex.getMessage());

        return ResponseEntity.badRequest().body(response);
    }
}
