package com.github.BMP29.oauth2_auth_provider.service.Impl;

import com.github.BMP29.oauth2_auth_provider.dto.ClientDto;
import com.github.BMP29.oauth2_auth_provider.dto.RegisterClientDto;
import com.github.BMP29.oauth2_auth_provider.service.IClientService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Arrays;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ClientServiceImpl implements IClientService {
    private RegisteredClientRepository clientRepository;
    private PasswordEncoder passwordEncoder;
    private SecureRandom secureRandom;

    private RegistrationAccessTokenServiceImpl registrationAccessTokenService;

   public ClientDto register(RegisterClientDto registerClientDto) {
        Long rawSecret = secureRandom.nextLong();
        String clientId = UUID.randomUUID().toString();

        RegisteredClient client = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId(clientId)
                .clientSecret(passwordEncoder.encode(rawSecret.toString()))
                .clientName(registerClientDto.name())
                .clientIdIssuedAt(Instant.now())
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .clientAuthenticationMethods((authMethods) ->
                        Arrays.stream(registerClientDto.authMethod())
                        .map(ClientAuthenticationMethod::new)
                        .forEach(authMethods::add)
                )
                .authorizationGrantTypes((grants) ->
                        Arrays.stream(registerClientDto.grantTypes())
                                .map(AuthorizationGrantType::new)
                                .forEach(grants::add)
                )
                .redirectUris((uris) ->
                        Arrays.stream(registerClientDto.redirectUri())
                                .map(String::new)
                                .forEach(uris::add)
                )
                .clientSettings(
                        ClientSettings.builder()
                        .requireAuthorizationConsent(true)
                        .requireProofKey(false)
                        .build()
                ).scope("read")
                .build();

       clientRepository.save(client);

       String registrationAccessToken =
               registrationAccessTokenService.create(client.getClientId());

       String registrationClientUri =
               "http://localhost:8080/connect/register/"
                       + client.getClientId();

       return new ClientDto(
               client.getClientId(),
               rawSecret.toString(),
               registrationAccessToken,
               registrationClientUri,
               client.getClientIdIssuedAt()
       );
    }
}
