package com.app.geoTracker.LocationController;

import com.app.geoTracker.dto.AccountabilityPartnerRequestDto;
import com.app.geoTracker.dto.AccountabilityPartnerResponseDto;
import com.app.geoTracker.service.AccountabilityPartnerService;
import com.app.geoTracker.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/partners")
@RequiredArgsConstructor
public class AccountabilityPartnerController {

    private final AccountabilityPartnerService partnerService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<AccountabilityPartnerResponseDto> create(@Valid @RequestBody AccountabilityPartnerRequestDto dto) {
        AccountabilityPartnerResponseDto created = partnerService.createPartner(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<AccountabilityPartnerResponseDto>> getAll() {
        return ResponseEntity.ok(partnerService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountabilityPartnerResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(partnerService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccountabilityPartnerResponseDto> update(@PathVariable Long id, @Valid @RequestBody AccountabilityPartnerRequestDto dto) {
        return ResponseEntity.ok(partnerService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        partnerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
