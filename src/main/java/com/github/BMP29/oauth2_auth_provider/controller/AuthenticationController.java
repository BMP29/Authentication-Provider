package com.github.BMP29.oauth2_auth_provider.controller;

import com.github.BMP29.oauth2_auth_provider.dto.UserDto;
import com.github.BMP29.oauth2_auth_provider.service.Impl.UserServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthenticationController {
    private UserServiceImpl userService;

    @PostMapping("/signup")
    public ResponseEntity<Void> register(@RequestBody UserDto userDto) {
        userService.createUser(userDto);

        return ResponseEntity
                .status(HttpStatus.CREATED).build();
    }
}
