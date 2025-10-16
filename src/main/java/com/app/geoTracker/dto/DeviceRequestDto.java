package com.app.geoTracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record DeviceRequestDto(
        @NotBlank(message = "Device name cannot be blank")
        @Size(max = 100, message = "Device name must not exceed 100 characters")
        String name,

        @NotBlank(message = "Device type cannot be blank")
        String type,

        @NotBlank(message = "Serial number cannot be blank")
        String serialNumber,

        @NotNull(message = "Owner ID is required")
        Long ownerId
) {}
