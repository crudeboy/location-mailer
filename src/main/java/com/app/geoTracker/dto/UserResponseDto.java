package com.app.geoTracker.dto;

import com.app.geoTracker.model.User;

public record UserResponseDto(
        Long id,
        String name,
        String email
) {
    public static UserResponseDto from(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }
}
