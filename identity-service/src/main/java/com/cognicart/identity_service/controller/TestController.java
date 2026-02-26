package com.cognicart.identity_service.controller;

import com.cognicart.identity_service.dto.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/user")
    public ApiResponse<String> userEndpoint() {
        return ApiResponse.<String>builder()
                .success(true)
                .message("User endpoint accessed")
                .data("User endpoint accessed")
                .build();
    }

    @GetMapping("/admin")
    public ApiResponse<String> adminEndpoint() {
        return ApiResponse.<String>builder()
                .success(true)
                .message("Admin endpoint accessed")
                .data("Admin endpoint accessed")
                .build();
    }
}
