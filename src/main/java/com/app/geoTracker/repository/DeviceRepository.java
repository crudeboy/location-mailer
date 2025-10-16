package com.app.geoTracker.repository;

import com.app.geoTracker.model.Device;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceRepository extends JpaRepository<Device, Long> {}
