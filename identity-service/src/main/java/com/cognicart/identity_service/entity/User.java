package com.cognicart.identity_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    private LocalDateTime createdAt;

    @Column(nullable = false, columnDefinition = "integer default 0")
    @lombok.Builder.Default
    private int failedAttempts = 0;

    @Column(nullable = false, columnDefinition = "boolean default false")
    @lombok.Builder.Default
    private boolean accountLocked = false;
}