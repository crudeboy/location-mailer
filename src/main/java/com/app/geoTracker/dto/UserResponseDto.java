package com.app.geoTracker.dto;

import com.app.geoTracker.model.User;

import java.util.List;
import java.util.stream.Collectors;

public record UserResponseDto(
        Long id,
        String name,
        String email,
        List<AccountabilityPartnerResponseDto> accountabilityPartners,
        List<DeviceResponseDto> devices
) {
    public static UserResponseDto from(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getAccountabilityPartners() != null
                        ? user.getAccountabilityPartners().stream()
                        .map(AccountabilityPartnerResponseDto::from)
                        .collect(Collectors.toList())
                        : List.of(),
                user.getDevices() != null
                        ? user.getDevices().stream()
                        .map(DeviceResponseDto::from)
                        .collect(Collectors.toList())
                        : List.of()
        );
    }
}
