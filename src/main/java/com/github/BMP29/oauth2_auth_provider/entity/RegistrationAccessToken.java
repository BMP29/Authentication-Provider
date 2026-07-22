package com.github.BMP29.oauth2_auth_provider.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationAccessToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "client_id")
    private String clientId;

    @NotBlank
    @Column(name = "token_hash")
    private String tokenHash;

    @NotNull
    @Column(name = "expires_at")
    private Instant expiresAt;

    @NotNull
    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "revoked")
    private boolean revoked = false;

}
