package com.github.BMP29.oauth2_auth_provider.controller;

import com.github.BMP29.oauth2_auth_provider.dto.*;
import com.github.BMP29.oauth2_auth_provider.entity.User;
import com.github.BMP29.oauth2_auth_provider.mapper.UserMapper;
import com.github.BMP29.oauth2_auth_provider.service.Impl.AuthenticationServiceImpl;
import com.github.BMP29.oauth2_auth_provider.service.Impl.UserServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthenticationController {
    private AuthenticationServiceImpl authService;

    @PostMapping("/signup")
    public ResponseEntity<StandardResponseDto<UserDto>> register(@RequestBody SignUpDto signUpInput) {
        User u = authService.signup(signUpInput);

        UserDto userDto = UserMapper.mapToUserDto(u);

        StandardResponseDto<UserDto> stdResponse =
                new StandardResponseDto<>(
                        true,
                        "Usuário criado com sucesso",
                        userDto);

        return new ResponseEntity<>(stdResponse, HttpStatus.CREATED);
    }
}
