package com.github.BMP29.oauth2_auth_provider.mapper;

import com.github.BMP29.oauth2_auth_provider.dto.UserDto;
import com.github.BMP29.oauth2_auth_provider.entity.User;

public class UserMapper {
    public static UserDto mapToUserDto(User user) {
        UserDto userDto = new UserDto(
                user.getUsername(),
                user.getEmail(),
                user.isEnabled(),
                user.getCreatedAt()
        );

        return userDto;
    }
    
}
