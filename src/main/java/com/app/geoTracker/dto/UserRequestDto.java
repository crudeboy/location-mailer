package com.app.geoTracker.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserRequestDto(
        @NotBlank(message = "Name is required")
        String name,

        @Email(message = "Email must be valid")
        @NotBlank(message = "Email is required")
        String email
) {}
