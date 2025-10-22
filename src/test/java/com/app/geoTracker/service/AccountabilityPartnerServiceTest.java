package com.app.geoTracker.service;

import com.app.geoTracker.dto.AccountabilityPartnerRequestDto;
import com.app.geoTracker.dto.AccountabilityPartnerResponseDto;
import com.app.geoTracker.exception.ApiException;
import com.app.geoTracker.model.AccountabilityPartner;
import com.app.geoTracker.repository.AccountabilityPartnerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccountabilityPartnerServiceTest {

    @Mock
    private AccountabilityPartnerRepository repository;

    @InjectMocks
    private AccountabilityPartnerService service;

    private AccountabilityPartner partner;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        partner = AccountabilityPartner.builder()
                .id(1L)
                .name("John Doe")
                .email("john@example.com")
                .build();
    }

    // ✅ Test: Create Partner (Happy Path)
    @Test
    void createPartner_ShouldSaveAndReturnResponseDto() {
        AccountabilityPartnerRequestDto dto = new AccountabilityPartnerRequestDto("John Doe", "john@example.com");
        when(repository.save(any(AccountabilityPartner.class))).thenReturn(partner);

        AccountabilityPartnerResponseDto result = service.createPartner(dto);

        assertThat(result.name()).isEqualTo("John Doe");
        assertThat(result.email()).isEqualTo("john@example.com");
        verify(repository, times(1)).save(any(AccountabilityPartner.class));
    }

    // ✅ Test: Get All Partners
    @Test
    void getAll_ShouldReturnMappedList() {
        when(repository.findAll()).thenReturn(List.of(partner));

        var result = service.getAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).email()).isEqualTo("john@example.com");
        verify(repository, times(1)).findAll();
    }

    // ✅ Test: Get Partner by ID (Happy Path)
    @Test
    void getById_ShouldReturnPartner_WhenExists() {
        when(repository.findById(1L)).thenReturn(Optional.of(partner));

        var result = service.getById(1L);

        assertThat(result.name()).isEqualTo("John Doe");
        verify(repository, times(1)).findById(1L);
    }

    // ⚠️ Test: Get Partner by ID (Not Found)
    @Test
    void getById_ShouldThrowException_WhenNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getById(1L))
                .isInstanceOf(ApiException.class)
                .hasMessage("Partner not found");
    }

    // ✅ Test: Update Partner (Happy Path)
    @Test
    void update_ShouldModifyAndSavePartner() {
        AccountabilityPartnerRequestDto dto = new AccountabilityPartnerRequestDto("Jane Doe", "jane@example.com");
        when(repository.findById(1L)).thenReturn(Optional.of(partner));
        when(repository.save(any(AccountabilityPartner.class))).thenReturn(partner);

        AccountabilityPartnerResponseDto result = service.update(1L, dto);

        assertThat(result.name()).isEqualTo("Jane Doe");
        assertThat(result.email()).isEqualTo("jane@example.com");
        verify(repository, times(1)).save(partner);
    }

    // ⚠️ Test: Update Partner (Not Found)
    @Test
    void update_ShouldThrowException_WhenPartnerNotFound() {
        AccountabilityPartnerRequestDto dto = new AccountabilityPartnerRequestDto("Jane Doe", "jane@example.com");
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update(99L, dto))
                .isInstanceOf(ApiException.class)
                .hasMessage("Partner not found");
    }

    // ✅ Test: Delete Partner (Happy Path)
    @Test
    void delete_ShouldCallRepositoryDelete() {
        doNothing().when(repository).deleteById(1L);

        service.delete(1L);

        verify(repository, times(1)).deleteById(1L);
    }

    // ⚠️ Test: Delete Partner (Repo Exception)
    @Test
    void delete_ShouldHandleRepositoryException() {
        doThrow(new RuntimeException("DB error")).when(repository).deleteById(1L);

        assertThatThrownBy(() -> service.delete(1L))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("DB error");
    }
}
