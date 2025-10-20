package com.app.geoTracker.dto;

import com.app.geoTracker.model.Device;

public record DeviceResponseDto(
        Long id,
        String type,
        String serialNumber,
        Long ownerId
) {
    public static DeviceResponseDto from(Device device) {
        return new DeviceResponseDto(
                device.getId(),
                device.getType(),
                device.getDeviceId(),
                device.getOwner().getId()
        );
    }
}
