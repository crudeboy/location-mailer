package com.app.geoTracker.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    @Builder.Default
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Device> devices = new HashSet<>();

    @Builder.Default
    @ManyToMany
    @JoinTable(
            name = "user_accountability_partners",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "partner_id")
    )
    private Set<AccountabilityPartner> accountabilityPartners = new HashSet<>();

    // --- Helper Methods ---
    public void addDevice(Device device) {
        devices.add(device);
        device.setOwner(this);
    }

    public void removeDevice(Device device) {
        devices.remove(device);
        device.setOwner(null);
    }

    public void addAccountabilityPartner(AccountabilityPartner partner) {
        accountabilityPartners.add(partner);
    }

    public void removeAccountabilityPartner(AccountabilityPartner partner) {
        accountabilityPartners.remove(partner);
    }
}
