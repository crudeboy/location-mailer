package com.app.geoTracker.controller;

import com.app.geoTracker.dto.UserRequestDto;
import com.app.geoTracker.dto.UserResponseDto;
import com.app.geoTracker.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAll() {
        return ResponseEntity.ok(userService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.get(id));
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> create(@Valid @RequestBody UserRequestDto dto) {
        return ResponseEntity.ok(userService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> update(
            @PathVariable Long id,
            @Valid @RequestBody UserRequestDto dto
    ) {
        return ResponseEntity.ok(userService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{userId}/accountability-partners/{apId}")
    public ResponseEntity<UserResponseDto> addAccountabilityPartner(@PathVariable Long userId, @PathVariable Long apId) {
        return ResponseEntity.ok(userService.addAccountabilityPartner(userId, apId));
    }

    @PostMapping("/{userId}/devices/{deviceId}")
    public ResponseEntity<UserResponseDto> addDevice(@PathVariable Long userId, @PathVariable Long deviceId) {
        return ResponseEntity.ok(userService.addDevice(userId, deviceId));
    }
}
