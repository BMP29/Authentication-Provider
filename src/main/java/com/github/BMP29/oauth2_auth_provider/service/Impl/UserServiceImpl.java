package com.github.BMP29.oauth2_auth_provider.service.Impl;

import com.github.BMP29.oauth2_auth_provider.dto.UserDto;
import com.github.BMP29.oauth2_auth_provider.entity.User;
import com.github.BMP29.oauth2_auth_provider.mapper.UserMapper;
import com.github.BMP29.oauth2_auth_provider.repository.UserRepository;
import com.github.BMP29.oauth2_auth_provider.service.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements IUserService {

    private UserRepository userRepository;

    @Override
    public void createUser(UserDto userDto) {
        User u = UserMapper.mapToUser(userDto, new User());
        Optional<User> optionalUser = userRepository.findByUsername(userDto.getUsername());

        if(optionalUser.isPresent()) {
            throw new RuntimeException("E-mail já está em uso" + userDto.getUsername());
        }

        User savedUser = userRepository.save(u);
    }
}
