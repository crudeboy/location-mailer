package com.app.geoTracker.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "devices")
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String deviceId;

    private String type; // e.g., "LAPTOP", "PHONE"

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User owner;
}
