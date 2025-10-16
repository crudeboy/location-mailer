package com.app.geoTracker.LocationController;

import com.app.geoTracker.dto.DeviceLocationRequest;
import com.app.geoTracker.service.LocationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/location")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @PostMapping("/update")
    public String updateLocation(@RequestBody DeviceLocationRequest request) {
        log.info("Updating the location for: {}", request.getDeviceId());
        locationService.processLocation(request);
        return "Location received successfully";
    }
}
