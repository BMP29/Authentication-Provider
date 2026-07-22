package com.github.BMP29.oauth2_auth_provider.dto;

public record RegisterClientDto(
        String name,
        String[] redirectUri,
        String[] grantTypes,
        String[] authMethod
) {
}
