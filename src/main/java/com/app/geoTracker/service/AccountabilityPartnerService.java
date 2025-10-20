package com.app.geoTracker.service;

import com.app.geoTracker.dto.AccountabilityPartnerRequestDto;
import com.app.geoTracker.dto.AccountabilityPartnerResponseDto;
import com.app.geoTracker.exception.ApiException;
import com.app.geoTracker.model.AccountabilityPartner;
import com.app.geoTracker.repository.AccountabilityPartnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountabilityPartnerService {

    private final AccountabilityPartnerRepository repository;

    public AccountabilityPartnerResponseDto createPartner(AccountabilityPartnerRequestDto dto) {
        AccountabilityPartner partner = AccountabilityPartner.builder()
                .name(dto.name())
                .email(dto.email())
                .build();

        AccountabilityPartner saved = repository.save(partner);
        return new AccountabilityPartnerResponseDto(saved.getId(), saved.getName(), saved.getEmail());
    }

    public List<AccountabilityPartnerResponseDto> getAll() {
        return repository.findAll()
                .stream()
                .map(p -> new AccountabilityPartnerResponseDto(p.getId(), p.getName(), p.getEmail()))
                .collect(Collectors.toList());
    }

    public AccountabilityPartnerResponseDto getById(Long id) {
        AccountabilityPartner p = repository.findById(id)
                .orElseThrow(() -> new ApiException("Partner not found", HttpStatus.BAD_REQUEST));
        return new AccountabilityPartnerResponseDto(p.getId(), p.getName(), p.getEmail());
    }

    public AccountabilityPartnerResponseDto update(Long id, AccountabilityPartnerRequestDto dto) {
        AccountabilityPartner partner = repository.findById(id)
                .orElseThrow(() -> new ApiException("Partner not found", HttpStatus.BAD_REQUEST));

        partner.setName(dto.name());
        partner.setEmail(dto.email());
        AccountabilityPartner updated = repository.save(partner);
        return new AccountabilityPartnerResponseDto(updated.getId(), updated.getName(), updated.getEmail());
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
