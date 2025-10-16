package com.app.geoTracker.service;

import com.app.geoTracker.dto.DeviceLocationRequest;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class LocationService {

    private static final double HOME_LAT = 6.5244;  // Example: Lagos
    private static final double HOME_LNG = 3.3792;
    private static final double RADIUS_METERS = 100;

    private final EmailService emailService;
    private final Map<String, Boolean> deviceInsideHome = new ConcurrentHashMap<>();

    @PostConstruct
    void init() {
        deviceInsideHome.put("LAPTOP", true);
    }


    public void processLocation(DeviceLocationRequest request) {
        boolean inside = isWithinHome(request.getLatitude(), request.getLongitude());
        Boolean wasInside = deviceInsideHome.get(request.getDeviceId());
        deviceInsideHome.put(request.getDeviceId(), inside);
        log.info("Device is inside: {}", inside);

        /**
         * Send email only when the device has a recorded location state,
          was previously inside the home zone, and is now outside.
         (If there's no prior record, or the device remains outside, no email is sent.)
         */
        Boolean deviceNotHomeAnyMore = wasInside != null && wasInside && !inside;
        log.info("Device is home: {}", deviceNotHomeAnyMore);

        if (deviceNotHomeAnyMore) {
            log.info("Device is out of Home location...");
            emailService.sendAlert(request.getDeviceId());
        }
    }

    private boolean isWithinHome(double lat, double lng) {
        double distance = distanceInMeters(lat, lng, HOME_LAT, HOME_LNG);
        return distance <= RADIUS_METERS;
    }

    private double distanceInMeters(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371000; // Earth's radius (m)
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}