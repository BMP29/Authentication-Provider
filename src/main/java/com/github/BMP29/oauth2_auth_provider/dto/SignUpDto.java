package com.github.BMP29.oauth2_auth_provider.dto;

import jakarta.validation.constraints.NotBlank;

public record SignUpDto (
    @NotBlank
    String username,
    @NotBlank
    String password,
    @NotBlank
    String email
) { }
