package com.app.geoTracker.dto;

public record DeviceResponseDto(
        Long id,
        String type,
        String serialNumber,
        Long ownerId
) {}
