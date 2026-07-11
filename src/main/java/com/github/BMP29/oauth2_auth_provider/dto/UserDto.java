package com.github.BMP29.oauth2_auth_provider.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record UserDto (
        @NotBlank
        String username,
        @NotBlank
        String email,
        @NotNull
        boolean enabled,
        @NotNull
        LocalDateTime createdAt
) { }
