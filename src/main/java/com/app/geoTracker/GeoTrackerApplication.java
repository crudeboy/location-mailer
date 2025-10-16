package com.app.geoTracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class GeoTrackerApplication {
    public static void main(String[] args) {
        SpringApplication.run(GeoTrackerApplication.class, args);
    }
}
