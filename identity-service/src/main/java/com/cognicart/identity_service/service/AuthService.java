package com.cognicart.identity_service.service;

import com.cognicart.identity_service.dto.LoginRequest;
import com.cognicart.identity_service.dto.RegisterRequest;
import com.cognicart.identity_service.entity.RefreshToken;
import com.cognicart.identity_service.entity.Role;
import com.cognicart.identity_service.entity.User;
import com.cognicart.identity_service.event.UserEventProducer;
import com.cognicart.identity_service.event.UserRegisteredEvent;
import com.cognicart.identity_service.exception.CustomException;
import com.cognicart.identity_service.repository.RefreshTokenRepository;
import com.cognicart.identity_service.repository.RoleRepository;
import com.cognicart.identity_service.repository.UserRepository;
import com.cognicart.identity_service.security.JwtService;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserEventProducer userEventProducer;

    public String register(RegisterRequest request) {

        log.info("User {} attempting registration", request.getEmail());

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            log.warn("Registration failed: Email {} already exists", request.getEmail());
            throw new CustomException("Email already exists");
        }

        Role role = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new CustomException("Default role not found"));

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .createdAt(LocalDateTime.now())
                .build();

        userRepository.save(user);

        log.info("User {} registered successfully", request.getEmail());

        userEventProducer.publishUserRegisteredEvent(
                UserRegisteredEvent.builder()
                        .userId(user.getId().toString())
                        .email(user.getEmail())
                        .role(user.getRole().getName())
                        .build()
        );

        return "User registered successfully";
    }
    @Transactional
    public Map<String, String> login(LoginRequest request) {

        log.info("User {} attempting login", request.getEmail());

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new CustomException("Invalid credentials"));

        // Check if account is locked
        if (user.isAccountLocked()) {
            log.warn("Login failed: Account {} is locked", request.getEmail());
            throw new CustomException("Account is locked. Try later.");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            // Increment failed attempts
            user.setFailedAttempts(user.getFailedAttempts() + 1);

            // Lock account after 5 failed attempts
            if (user.getFailedAttempts() >= 5) {
                user.setAccountLocked(true);
            }
            userRepository.save(user);

            log.warn("Login failed: Invalid credentials for {}", request.getEmail());

            throw new CustomException("Invalid credentials");
        }

        // Reset failed attempts on successful login
        user.setFailedAttempts(0);
        userRepository.save(user);

        log.info("User {} logged in successfully", request.getEmail());

        String accessToken = jwtService.generateAccessToken(
                user.getEmail(),
                user.getRole().getName()
        );

        String refreshTokenValue = jwtService.generateRefreshToken(user.getEmail());

        refreshTokenRepository.deleteByUserId(user.getId());

        RefreshToken refreshToken = RefreshToken.builder()
                .token(refreshTokenValue)
                .user(user)
                .expiryDate(LocalDateTime.now().plusDays(7))
                .build();

        refreshTokenRepository.save(refreshToken);

        return Map.of(
                "accessToken", accessToken,
                "refreshToken", refreshTokenValue
        );
    }

    public Map<String, String> refresh(String refreshTokenValue) {

        RefreshToken refreshToken = refreshTokenRepository.findByToken(refreshTokenValue)
                .orElseThrow(() -> new CustomException("Invalid refresh token"));

        if (refreshToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new CustomException("Refresh token expired");
        }

        User user = refreshToken.getUser();

        String newAccessToken = jwtService.generateAccessToken(
                user.getEmail(),
                user.getRole().getName()
        );

        return Map.of("accessToken", newAccessToken);
    }
}