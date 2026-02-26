package com.cognicart.identity_service.service;

import com.cognicart.identity_service.dto.LoginRequest;
import com.cognicart.identity_service.dto.RegisterRequest;
import com.cognicart.identity_service.entity.RefreshToken;
import com.cognicart.identity_service.entity.Role;
import com.cognicart.identity_service.entity.User;
import com.cognicart.identity_service.exception.CustomException;
import com.cognicart.identity_service.repository.RefreshTokenRepository;
import com.cognicart.identity_service.repository.RoleRepository;
import com.cognicart.identity_service.repository.UserRepository;
import com.cognicart.identity_service.security.JwtService;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;

    public String register(RegisterRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
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

        return "User registered successfully";
    }
    @Transactional
    public Map<String, String> login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new CustomException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new CustomException("Invalid credentials");
        }

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