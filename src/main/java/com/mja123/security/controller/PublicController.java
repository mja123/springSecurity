package com.mja123.security.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Public controller with endpoints that don't require authentication.
 * These endpoints are accessible without Auth0 JWT tokens.
 */
@RestController
@RequestMapping("/public")
public class PublicController {

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        return ResponseEntity.ok(Map.of(
            "status", "UP",
            "timestamp", LocalDateTime.now(),
            "service", "Spring Security API"
        ));
    }

    @GetMapping("/info")
    public ResponseEntity<Map<String, String>> info() {
        return ResponseEntity.ok(Map.of(
            "name", "Spring Security API",
            "version", "1.0.0",
            "description", "REST API secured with Auth0 and Spring Security"
        ));
    }

    @GetMapping("/welcome")
    public ResponseEntity<Map<String, String>> welcome() {
        return ResponseEntity.ok(Map.of(
            "message", "Welcome to the public API endpoint!",
            "note", "This endpoint is publicly accessible without authentication."
        ));
    }
}

