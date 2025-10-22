package com.app.geoTracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceLocationRequest {
    private String deviceId;  // e.g. "PHONE" or "LAPTOP"
    private double latitude;
    private double longitude;
}
