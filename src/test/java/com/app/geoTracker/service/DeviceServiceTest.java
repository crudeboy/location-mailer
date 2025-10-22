package com.app.geoTracker.service;

import com.app.geoTracker.dto.DeviceRequestDto;
import com.app.geoTracker.dto.DeviceResponseDto;
import com.app.geoTracker.exception.ApiException;
import com.app.geoTracker.model.Device;
import com.app.geoTracker.model.User;
import com.app.geoTracker.repository.DeviceRepository;
import com.app.geoTracker.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class DeviceServiceTest {

    @Mock
    private DeviceRepository deviceRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private DeviceService deviceService;

    private User user;
    private Device device;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = User.builder()
                .id(1L)
                .name("John Doe")
                .email("john@example.com")
                .build();

        device = Device.builder()
                .id(2L)
                .deviceId("ABC123")
                .type("MOBILE")
                .owner(user)
                .build();
    }

    // ✅ HAPPY CASES ------------------------------------------------------------

    @Test
    void createDevice_ShouldSaveDeviceAndReturnResponse() {
        DeviceRequestDto dto = new DeviceRequestDto("MOBILE", "ABC123", "123", 1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(deviceRepository.save(any(Device.class))).thenReturn(device);

        DeviceResponseDto result = deviceService.createDevice(dto);

        assertThat(result.serialNumber()).isEqualTo("ABC123");
        assertThat(result.ownerId()).isEqualTo(1L);
        verify(userRepository).findById(1L);
        verify(deviceRepository).save(any(Device.class));
    }

    @Test
    void getAllDevices_ShouldReturnDeviceList() {
        when(deviceRepository.findAll()).thenReturn(List.of(device));

        var result = deviceService.getAllDevices();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).type()).isEqualTo("MOBILE");
        assertThat(result.get(0).ownerId()).isEqualTo(1L);
        verify(deviceRepository).findAll();
    }

    @Test
    void getDevice_ShouldReturnDevice_WhenExists() {
        when(deviceRepository.findById(2L)).thenReturn(Optional.of(device));

        var result = deviceService.getDevice(2L);

        assertThat(result.serialNumber()).isEqualTo("ABC123");
        verify(deviceRepository).findById(2L);
    }

    @Test
    void updateDevice_ShouldUpdateDevice_WhenExists() {
        DeviceRequestDto dto = new DeviceRequestDto("LAPTOP", "XYZ789", "123", 1L);
        when(deviceRepository.findById(2L)).thenReturn(Optional.of(device));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(deviceRepository.save(any(Device.class))).thenReturn(device);

        var result = deviceService.updateDevice(2L, dto);

        assertThat(result.type()).isEqualTo("MOBILE"); // unchanged because of mock
        verify(deviceRepository).save(any(Device.class));
    }

    @Test
    void deleteDevice_ShouldCallRepositoryDelete() {
        doNothing().when(deviceRepository).deleteById(2L);

        deviceService.deleteDevice(2L);

        verify(deviceRepository).deleteById(2L);
    }

    // ⚠️ EDGE & ERROR CASES ------------------------------------------------------

    @Test
    void createDevice_ShouldThrow_WhenOwnerNotFound() {
        DeviceRequestDto dto = new DeviceRequestDto("MOBILE", "ABC123", "123", 99L);
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> deviceService.createDevice(dto))
                .isInstanceOf(ApiException.class)
                .hasMessageContaining("User not found");
    }

    @Test
    void getDevice_ShouldThrow_WhenNotFound() {
        when(deviceRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> deviceService.getDevice(99L))
                .isInstanceOf(ApiException.class)
                .hasMessageContaining("Device not found");
    }

    @Test
    void updateDevice_ShouldThrow_WhenDeviceNotFound() {
        DeviceRequestDto dto = new DeviceRequestDto("LAPTOP", "XYZ", "123", 1L);
        when(deviceRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> deviceService.updateDevice(99L, dto))
                .isInstanceOf(ApiException.class)
                .hasMessageContaining("Device not found");
    }

    @Test
    void updateDevice_ShouldThrow_WhenOwnerNotFound() {
        DeviceRequestDto dto = new DeviceRequestDto("LAPTOP", "XYZ", "123",99L);
        when(deviceRepository.findById(2L)).thenReturn(Optional.of(device));
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> deviceService.updateDevice(2L, dto))
                .isInstanceOf(ApiException.class)
                .hasMessageContaining("User not found");
    }

    @Test
    void getAllDevices_ShouldHandleEmptyList() {
        when(deviceRepository.findAll()).thenReturn(List.of());

        var result = deviceService.getAllDevices();

        assertThat(result).isEmpty();
        verify(deviceRepository).findAll();
    }

    @Test
    void getAllDevices_ShouldHandleNullOwnerGracefully() {
        device.setOwner(null);
        when(deviceRepository.findAll()).thenReturn(List.of(device));

        var result = deviceService.getAllDevices();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).ownerId()).isNull();
    }

    @Test
    void updateDevice_ShouldAllowNullOwnerId() {
        DeviceRequestDto dto = new DeviceRequestDto("LAPTOP", "XYZ789", null, 1L);
        when(deviceRepository.findById(2L)).thenReturn(Optional.of(device));
        when(deviceRepository.save(any(Device.class))).thenReturn(device);

        var result = deviceService.updateDevice(2L, dto);

        assertThat(result).isNotNull();
        verify(deviceRepository).save(any(Device.class));
    }
}
