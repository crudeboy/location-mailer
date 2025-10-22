package com.app.geoTracker.service;

import com.app.geoTracker.dto.UserRequestDto;
import com.app.geoTracker.model.AccountabilityPartner;
import com.app.geoTracker.model.Device;
import com.app.geoTracker.model.User;
import com.app.geoTracker.exception.ApiException;
import com.app.geoTracker.repository.AccountabilityPartnerRepository;
import com.app.geoTracker.repository.DeviceRepository;
import com.app.geoTracker.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private AccountabilityPartnerRepository apRepository;
    @Mock
    private DeviceRepository deviceRepository;

    @InjectMocks
    private UserService userService;

    private User user;
    private AccountabilityPartner ap;
    private Device device;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = User.builder()
                .id(1L)
                .name("John Doe")
                .email("john@example.com")
                .build();

        ap = AccountabilityPartner.builder()
                .id(10L)
                .name("Paul Partner")
                .build();

        device = Device.builder()
                .id(20L)
                .type("PHONE")
                .build();
    }

    @Test
    void getAllUsers_ShouldReturnMappedResponseDtos() {
        when(userRepository.findAll()).thenReturn(List.of(user));

        var result = userService.getAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).name()).isEqualTo("John Doe");
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void getUser_ShouldReturnUser_WhenFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        var result = userService.get(1L);

        assertThat(result.name()).isEqualTo("John Doe");
        verify(userRepository).findById(1L);
    }

    @Test
    void getUser_ShouldThrow_WhenNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        ApiException ex = assertThrows(ApiException.class, () -> userService.get(1L));
        assertThat(ex.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void createUser_ShouldSaveAndReturnDto() {
        var dto = new UserRequestDto("Jane Doe", "jane@example.com");
        var saved = User.builder().id(2L).name("Jane Doe").email("jane@example.com").build();

        when(userRepository.save(any(User.class))).thenReturn(saved);

        var result = userService.create(dto);

        assertThat(result.name()).isEqualTo("Jane Doe");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void updateUser_ShouldUpdateExistingUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        var dto = new UserRequestDto("Updated", "updated@example.com");
        var result = userService.update(1L, dto);

        assertThat(result.name()).isEqualTo("Updated");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void updateUser_ShouldThrow_WhenUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ApiException.class, () -> userService.update(1L, new UserRequestDto("n", "e")));
    }

    @Test
    void addApToUser_ShouldAddAndSaveUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(apRepository.findById(10L)).thenReturn(Optional.of(ap));
        when(userRepository.save(user)).thenReturn(user);

        var result = userService.addAccountabilityPartner(1L, 10L);

        assertThat(result.name()).isEqualTo("John Doe");
        verify(userRepository).save(user);
    }

    @Test
    void addApToUser_ShouldThrow_WhenUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ApiException.class, () -> userService.addAccountabilityPartner(1L, 10L));
    }

    @Test
    void addDeviceToUser_ShouldAddAndSaveUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(deviceRepository.findById(20L)).thenReturn(Optional.of(device));
        when(userRepository.save(user)).thenReturn(user);

        var result = userService.addDevice(1L, 20L);

        assertThat(result.name()).isEqualTo("John Doe");
        verify(userRepository).save(user);
    }

    @Test
    void deleteUser_ShouldInvokeRepositoryDelete() {
        doNothing().when(userRepository).deleteById(1L);

        userService.deleteUser(1L);

        verify(userRepository).deleteById(1L);
    }
}
