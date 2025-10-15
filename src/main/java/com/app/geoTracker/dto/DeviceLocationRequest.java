package com.app.geoTracker.dto;

import lombok.Data;

@Data
public class DeviceLocationRequest {
    private String deviceId;  // e.g. "PHONE" or "LAPTOP"
    private double latitude;
    private double longitude;
}
