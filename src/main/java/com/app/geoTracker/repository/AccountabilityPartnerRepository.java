package com.app.geoTracker.repository;

import com.app.geoTracker.model.AccountabilityPartner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountabilityPartnerRepository extends JpaRepository<AccountabilityPartner, Long> {
    Optional<AccountabilityPartner> findByEmail(String email);
}
