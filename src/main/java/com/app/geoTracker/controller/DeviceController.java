package com.app.geoTracker.controller;

import com.app.geoTracker.dto.DeviceRequestDto;
import com.app.geoTracker.dto.DeviceResponseDto;
import com.app.geoTracker.dto.MessageResponseDto;
import com.app.geoTracker.service.DeviceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/devices")
public class DeviceController {

    private final DeviceService deviceService;

    @PostMapping
    public ResponseEntity<DeviceResponseDto> create(@RequestBody DeviceRequestDto dto) {
        DeviceResponseDto device = deviceService.createDevice(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(device);
    }

    @GetMapping
    public ResponseEntity<List<DeviceResponseDto>> getAll() {
        return ResponseEntity.ok(deviceService.getAllDevices());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeviceResponseDto> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(deviceService.getDevice(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DeviceResponseDto> update(@PathVariable Long id, @RequestBody DeviceRequestDto dto) {
        return ResponseEntity.ok(deviceService.updateDevice(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponseDto> delete(@PathVariable Long id) {
        deviceService.deleteDevice(id);
        return ResponseEntity.ok(new MessageResponseDto("Device successfully deleted."));
    }
}
