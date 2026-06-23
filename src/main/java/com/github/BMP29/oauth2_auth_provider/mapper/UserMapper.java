package com.github.BMP29.oauth2_auth_provider.mapper;

import com.github.BMP29.oauth2_auth_provider.dto.UserDto;
import com.github.BMP29.oauth2_auth_provider.entity.User;

public class UserMapper {
    public static UserDto mapToUserDto(User user, UserDto userDto) {
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setPassword(user.getPassword());

        return userDto;
    }

    public static User mapToUser(UserDto userDto, User user) {
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());

        return user;
    }
}
