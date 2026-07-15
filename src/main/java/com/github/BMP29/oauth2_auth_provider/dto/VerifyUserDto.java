package com.github.BMP29.oauth2_auth_provider.dto;

public record VerifyUserDto (
        String email,
        String verificationCode
) {
}
