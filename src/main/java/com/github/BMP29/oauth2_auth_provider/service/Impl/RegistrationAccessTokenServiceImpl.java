package com.github.BMP29.oauth2_auth_provider.service.Impl;

import com.github.BMP29.oauth2_auth_provider.entity.RegistrationAccessToken;
import com.github.BMP29.oauth2_auth_provider.repository.RegistrationAccessTokenRepository;
import com.github.BMP29.oauth2_auth_provider.service.IRegistrationAccessTokenService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@AllArgsConstructor
@Service
public class RegistrationAccessTokenServiceImpl
        implements IRegistrationAccessTokenService {
    private RegistrationAccessTokenRepository registrationAccessTokenRepository;
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public String create(String clientId) {
        RegistrationAccessToken token = new RegistrationAccessToken();

        String rawToken = UUID.randomUUID().toString();

        token.setClientId(clientId);
        token.setTokenHash(passwordEncoder.encode(rawToken));
        token.setExpiresAt(Instant.now().plus(30, ChronoUnit.DAYS));
        token.setCreatedAt(Instant.now());

        registrationAccessTokenRepository.save(token);

        return rawToken;
    }
}
