package com.github.BMP29.oauth2_auth_provider.repository;

import com.github.BMP29.oauth2_auth_provider.entity.RegistrationAccessToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegistrationAccessTokenRepository
        extends JpaRepository<RegistrationAccessToken, Long> {
}
