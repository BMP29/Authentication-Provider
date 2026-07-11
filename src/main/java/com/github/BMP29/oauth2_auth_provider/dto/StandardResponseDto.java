package com.github.BMP29.oauth2_auth_provider.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record StandardResponseDto<T>(
        @NotNull
        boolean success,
        @NotBlank
        String message,
        @NotNull
        T data
) {
}
