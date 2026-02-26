package com.cognicart.identity_service.controller;

import com.cognicart.identity_service.dto.ApiResponse;
import com.cognicart.identity_service.dto.LoginRequest;
import com.cognicart.identity_service.dto.RegisterRequest;
import com.cognicart.identity_service.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ApiResponse<String> register(@RequestBody RegisterRequest request) {
        String message = authService.register(request);
        return ApiResponse.<String>builder()
                .success(true)
                .message("Registration successful")
                .data(message)
                .build();
    }

    @PostMapping("/login")
    public ApiResponse<Map<String, String>> login(@RequestBody LoginRequest request) {
        Map<String, String> tokens = authService.login(request);
        return ApiResponse.<Map<String, String>>builder()
                .success(true)
                .message("Login successful")
                .data(tokens)
                .build();
    }

    @PostMapping("/refresh")
    public ApiResponse<Map<String, String>> refresh(@RequestBody Map<String, String> request) {
        Map<String, String> token = authService.refresh(request.get("refreshToken"));
        return ApiResponse.<Map<String, String>>builder()
                .success(true)
                .message("Token refreshed successfully")
                .data(token)
                .build();
    }
}