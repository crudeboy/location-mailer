package com.app.geoTracker.dto;

import com.app.geoTracker.model.AccountabilityPartner;

public record AccountabilityPartnerResponseDto(
        Long id,
        String name,
        String email
) {
    public static AccountabilityPartnerResponseDto from(AccountabilityPartner ap) {
        return new AccountabilityPartnerResponseDto(
                ap.getId(),
                ap.getName(),
                ap.getEmail()
        );
    }
}
