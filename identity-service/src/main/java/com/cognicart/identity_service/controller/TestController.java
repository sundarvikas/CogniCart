package com.cognicart.identity_service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/user")
    public String userEndpoint() {
        return "User endpoint accessed";
    }

    @GetMapping("/admin")
    public String adminEndpoint() {
        return "Admin endpoint accessed";
    }
}
