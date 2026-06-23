package com.github.BMP29.oauth2_auth_provider.repository;

import com.github.BMP29.oauth2_auth_provider.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
