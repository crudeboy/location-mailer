package com.app.geoTracker.service;

import com.app.geoTracker.dto.UserRequestDto;
import com.app.geoTracker.dto.UserResponseDto;
import com.app.geoTracker.exception.ApiException;
import com.app.geoTracker.model.AccountabilityPartner;
import com.app.geoTracker.model.Device;
import com.app.geoTracker.model.User;
import com.app.geoTracker.repository.AccountabilityPartnerRepository;
import com.app.geoTracker.repository.DeviceRepository;
import com.app.geoTracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service @Slf4j
@RequiredArgsConstructor
public class UserService {
    //TODO: Add a Home model, then home logic to this, a user has a home and location is checked based on teh home

    private final UserRepository userRepository;
    private final AccountabilityPartnerRepository apRepository;
    private final DeviceRepository deviceRepository;

    public List<UserResponseDto> getAll() {
        return userRepository.findAll()
                .stream()
                .map(UserResponseDto::from)
                .toList();
    }

    public UserResponseDto get(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ApiException("User not found", HttpStatus.NOT_FOUND));
        return UserResponseDto.from(user);
    }

    public UserResponseDto create(UserRequestDto dto) {
        User user = User.builder()
                .name(dto.name())
                .email(dto.email())
                .build();
        return UserResponseDto.from(userRepository.save(user));
    }

    public UserResponseDto update(Long id, UserRequestDto dto ) {
        User existing = userRepository.findById(id)
                .orElseThrow(() -> new ApiException("User not found", HttpStatus.NOT_FOUND));
        existing.setName(dto.name());
        existing.setEmail(dto.email());
        return UserResponseDto.from(userRepository.save(existing));
    }

    /**
    TODO
     1. Add a simple check to see if teh Ap already exists with the user
     2. Add rules to the class, only two Ap's
     3. Only three devices
     */
    public UserResponseDto addAccountabilityPartner(Long userId, Long apId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException("User not found", HttpStatus.NOT_FOUND));
        AccountabilityPartner ap = apRepository.findById(apId)
                .orElseThrow(() -> new ApiException("Accountability partner not found", HttpStatus.NOT_FOUND));

        boolean alreadyLinked = user.getAccountabilityPartners().stream()
                .anyMatch(d -> d.getId().equals(apId));

        if (alreadyLinked) {
            throw new ApiException("Accountability partner already assigned to user", HttpStatus.BAD_REQUEST);
        }

        user.addAccountabilityPartner(ap);

        return UserResponseDto.from(userRepository.save(user));
    }

    public UserResponseDto addDevice(Long userId, Long deviceId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException("User not found", HttpStatus.NOT_FOUND));
        Device device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new ApiException("Device not found", HttpStatus.NOT_FOUND));

        boolean alreadyLinked = user.getDevices().stream()
                .anyMatch(d -> d.getId().equals(deviceId));

        if (alreadyLinked) {
            throw new ApiException("Device already assigned to user", HttpStatus.BAD_REQUEST);
        }

        user.addDevice(device);

        return UserResponseDto.from(userRepository.save(user));
    }

    public void deleteUser(Long id) {
        userRepository.findById(id)
                .orElseThrow(() -> new ApiException("User not found, already deleted.", HttpStatus.NOT_FOUND));
        userRepository.deleteById(id);
        log.info("User successfully deleted: {}", id);
    }


}