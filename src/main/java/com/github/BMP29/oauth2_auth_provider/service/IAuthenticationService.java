package com.github.BMP29.oauth2_auth_provider.service;

import com.github.BMP29.oauth2_auth_provider.dto.SignUpDto;
import com.github.BMP29.oauth2_auth_provider.entity.User;

public interface IAuthenticationService {
    public User signup(SignUpDto signUpInput);
}
