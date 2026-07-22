package com.github.BMP29.oauth2_auth_provider.controller;

import com.github.BMP29.oauth2_auth_provider.dto.RegisterClientDto;
import com.github.BMP29.oauth2_auth_provider.dto.ClientDto;
import com.github.BMP29.oauth2_auth_provider.dto.StandardResponseDto;
import com.github.BMP29.oauth2_auth_provider.service.Impl.ClientServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/connect")
public class ClientController {
    private ClientServiceImpl clientService;

    @PostMapping("/register")
    public ResponseEntity<StandardResponseDto<ClientDto>> register(@RequestBody RegisterClientDto registerClientDto) {
        ClientDto clientDto =
                clientService.register(registerClientDto);

        StandardResponseDto<ClientDto> standardResponse =
                new StandardResponseDto<>(
                        true,
                        "Cliente registrado com sucesso.",
                        clientDto
                );

        return new ResponseEntity<>(standardResponse, HttpStatus.CREATED);
    }
}
