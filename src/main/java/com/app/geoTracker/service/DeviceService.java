package com.app.geoTracker.service;

import com.app.geoTracker.dto.DeviceRequestDto;
import com.app.geoTracker.dto.DeviceResponseDto;
import com.app.geoTracker.model.Device;
import com.app.geoTracker.model.User;
import com.app.geoTracker.repository.DeviceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.app.geoTracker.repository.UserRepository;


import java.util.List;
import java.util.stream.Collectors;

@Service @Slf4j
@RequiredArgsConstructor
public class DeviceService {

    private final DeviceRepository deviceRepository;
    private final UserRepository userRepository;

    public DeviceResponseDto createDevice(DeviceRequestDto dto) {
        User owner = userRepository.findById(dto.ownerId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Device device =  Device.builder()
                .deviceId(dto.serialNumber())
                .type(dto.type())
                .owner(owner)
                .build();
        deviceRepository.save(device);

        return new DeviceResponseDto(
                device.getId(),
                device.getType(),
                device.getDeviceId(),
                owner.getId()
        );
    }

    public List<DeviceResponseDto> getAllDevices() {
        return deviceRepository.findAll().stream()
                .map(device -> new DeviceResponseDto(
                        device.getId(),
                        device.getType(),
                        device.getDeviceId(),
                        device.getOwner() != null ? device.getOwner().getId() : null
                ))
                .collect(Collectors.toList());
    }

    public DeviceResponseDto getDevice(Long id) {
        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Device not found"));
        return new DeviceResponseDto(
                device.getId(),
                device.getType(),
                device.getDeviceId(),
                device.getOwner() != null ? device.getOwner().getId() : null
        );
    }

    public DeviceResponseDto updateDevice(Long id, DeviceRequestDto dto) {
        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Device not found"));

        device.setType(dto.type());
        device.setDeviceId(dto.serialNumber());

        if (dto.ownerId() != null) {
            User owner = userRepository.findById(dto.ownerId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            device.setOwner(owner);
        }

        deviceRepository.save(device);

        return new DeviceResponseDto(
                device.getId(),
                device.getType(),
                device.getDeviceId(),
                device.getOwner() != null ? device.getOwner().getId() : null
        );
    }

    public void deleteDevice(Long id) {
        deviceRepository.deleteById(id);
    }
}
