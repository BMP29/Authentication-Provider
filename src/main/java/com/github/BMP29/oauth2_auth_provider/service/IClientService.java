package com.github.BMP29.oauth2_auth_provider.service;

import com.github.BMP29.oauth2_auth_provider.dto.ClientDto;
import com.github.BMP29.oauth2_auth_provider.dto.RegisterClientDto;

public interface IClientService {
    ClientDto register(RegisterClientDto registerClientDto);
}
