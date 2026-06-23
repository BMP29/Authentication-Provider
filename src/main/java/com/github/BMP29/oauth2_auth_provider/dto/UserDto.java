package com.github.BMP29.oauth2_auth_provider.dto;

import lombok.Data;

@Data
public class UserDto {
    private String username;
    private String password;
    private String email;
}
