package com.app.geoTracker.service;

import com.app.geoTracker.dto.UserRequestDto;
import com.app.geoTracker.dto.UserResponseDto;
import com.app.geoTracker.model.User;
import com.app.geoTracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service @Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserResponseDto::from)
                .toList();
    }

    public UserResponseDto getUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return UserResponseDto.from(user);
    }

    public UserResponseDto createUser(UserRequestDto dto) {
        User user = User.builder()
                .name(dto.name())
                .email(dto.email())
                .build();
        return UserResponseDto.from(userRepository.save(user));
    }

    public UserResponseDto updateUser(Long id, UserRequestDto dto) {
        User existing = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        existing.setName(dto.name());
        existing.setEmail(dto.email());
        return UserResponseDto.from(userRepository.save(existing));
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}