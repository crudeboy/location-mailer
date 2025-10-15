package com.app.geoTracker.LocationController;

import com.app.geoTracker.dto.DeviceLocationRequest;
import com.app.geoTracker.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/location")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @PostMapping("/update")
    public String updateLocation(@RequestBody DeviceLocationRequest request) {
        locationService.processLocation(request);
        return "Location received successfully";
    }
}
