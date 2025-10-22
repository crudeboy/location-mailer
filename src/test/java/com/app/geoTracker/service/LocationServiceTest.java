package com.app.geoTracker.service;

import com.app.geoTracker.dto.DeviceLocationRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import static org.mockito.Mockito.*;

class LocationServiceTest {

    @Mock
    private EmailService emailService;

    @InjectMocks
    private LocationService locationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // ensure @PostConstruct runs
        locationService.init();
    }

    // ✅ Test: Device moves from inside → outside triggers alert
    @Test
    void processLocation_ShouldSendEmail_WhenDeviceLeavesHome() {
        // given device starts inside home (already seeded)
        DeviceLocationRequest request = new DeviceLocationRequest("LAPTOP", 6.5344, 3.3892); // ~1km away (outside radius)

        // when
        locationService.processLocation(request);

        // then
        verify(emailService, times(1)).sendAlert("LAPTOP");
    }

    // ⚙️ Test: Device new (no previous record) outside home → no email
    @Test
    void processLocation_ShouldNotSendEmail_WhenNewDeviceOutside() {
        DeviceLocationRequest request = new DeviceLocationRequest("PHONE", 6.6000, 3.5000); // far outside

        locationService.processLocation(request);

        verify(emailService, never()).sendAlert(any());
    }

    // ⚙️ Test: Device inside home (new or existing) → no email
    @Test
    void processLocation_ShouldNotSendEmail_WhenDeviceInside() {
        DeviceLocationRequest request = new DeviceLocationRequest("TABLET", 6.5244, 3.3792); // exactly home location

        locationService.processLocation(request);

        verify(emailService, never()).sendAlert(any());
    }

    // ⚙️ Test: Device was already outside → stays outside → no email
    @Test
    void processLocation_ShouldNotSendEmail_WhenDeviceStaysOutside() {
        DeviceLocationRequest request = new DeviceLocationRequest("PHONE", 6.6000, 3.5000);

        // first call — outside, recorded
        locationService.processLocation(request);
        // second call — still outside
        locationService.processLocation(request);

        verify(emailService, never()).sendAlert(any());
    }

    // ⚙️ Test: Device leaves and re-enters → email only once
    @Test
    void processLocation_ShouldSendEmailOnce_WhenDeviceLeavesThenReenters() {
        // step 1: leave home (inside → outside)
        DeviceLocationRequest leave = new DeviceLocationRequest("LAPTOP", 6.5344, 3.3892);
        locationService.processLocation(leave);

        // step 2: re-enter home (outside → inside)
        DeviceLocationRequest back = new DeviceLocationRequest("LAPTOP", 6.5244, 3.3792);
        locationService.processLocation(back);

        verify(emailService, times(1)).sendAlert("LAPTOP");
    }

    // ⚙️ Test: Distance computation sanity check (edge)
    @Test
    void processLocation_ShouldConsiderWithinRadiusCorrectly() {
        DeviceLocationRequest closePoint = new DeviceLocationRequest("NEAR", 6.5245, 3.3793);
        locationService.processLocation(closePoint);

        // No alert expected because device is still inside
        verify(emailService, never()).sendAlert(any());
    }
}
