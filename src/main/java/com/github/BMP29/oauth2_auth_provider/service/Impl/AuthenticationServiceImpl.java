package com.github.BMP29.oauth2_auth_provider.service.Impl;

import com.github.BMP29.oauth2_auth_provider.dto.SignUpDto;
import com.github.BMP29.oauth2_auth_provider.entity.User;
import com.github.BMP29.oauth2_auth_provider.repository.UserRepository;
import com.github.BMP29.oauth2_auth_provider.service.IAuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthenticationServiceImpl implements IAuthenticationService {

    private UserRepository userRepository;
    private AuthenticationManager authenticationManager;
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public User signup(SignUpDto signUpInput) {
        User user = new User();
        user.setUsername(signUpInput.username());
        user.setEmail(signUpInput.email());
        user.setPassword(passwordEncoder.encode(signUpInput.password()));
        user.setEnabled(false);
        user.setCreatedAt(LocalDateTime.now());
        user.setVerificationCodeExpiresAt(LocalDateTime.now().plusMinutes(15));
        user.setVerificationCode(UUID.randomUUID().toString());

        return userRepository.save(user);
    }
}
