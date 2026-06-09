package com.mja123.security.controller;

import com.mja123.security.domain.dto.LoginRequestDTO;
import com.mja123.security.domain.dto.LoginResponseDTO;
import com.mja123.security.domain.dto.RefreshTokenRequestDTO;
import com.mja123.security.domain.dto.SignUpRequestDTO;
import com.mja123.security.domain.dto.SignUpResponseDTO;
import com.mja123.security.domain.service.AuthService;
import com.mja123.security.exceptions.NotUniqueAttributeException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO request) {
        return ResponseEntity.ok(authService.login(request.email(), request.password()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDTO> refresh(@RequestBody @Valid RefreshTokenRequestDTO request) {
        return ResponseEntity.ok(authService.refresh(request.refreshToken()));
    }

    @PostMapping("/signup")
    public ResponseEntity<SignUpResponseDTO> signUp(@RequestBody @Valid SignUpRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.signUp(request));
    }
}
