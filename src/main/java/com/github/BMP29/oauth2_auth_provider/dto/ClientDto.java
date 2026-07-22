package com.github.BMP29.oauth2_auth_provider.dto;

import java.time.Instant;

public record ClientDto(
        String clientId,
        String clientSecret,
        String registrationAccessToken,
        String registrationClientUri,
        Instant clientIdIssuedAt
) {
}
