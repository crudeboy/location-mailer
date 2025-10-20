package com.app.geoTracker.LocationController;

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
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUser(id));
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserRequestDto dto) {
        return ResponseEntity.ok(userService.createUser(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserRequestDto dto
    ) {
        return ResponseEntity.ok(userService.updateUser(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{userId}/accountability-partners/{apId}")
    public ResponseEntity<UserResponseDto> addAp(@PathVariable Long userId, @PathVariable Long apId) {
        return ResponseEntity.ok(userService.addApToUser(userId, apId));
    }

    @PostMapping("/{userId}/device/{deviceId}")
    public ResponseEntity<UserResponseDto> addDevice(@PathVariable Long userId, @PathVariable Long deviceId) {
        return ResponseEntity.ok(userService.addDeviceToUser(userId, deviceId));
    }
}
