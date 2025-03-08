package com.blu.device;

import com.blu.path.Path;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="registered_devices")
public class RegisteredDevice {

//    @Id
//    @Column(name="mac_address",
//            length=24,
//            nullable=false)
//    private String macAddress;

    @Id
    @Column(name="ip_address",
            nullable=false)
    private String ipAddress;

    @Column(nullable=false)
    private boolean active;

    private int size;

    @OneToMany(
            mappedBy = "registeredDevice",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )

    private List<Path> paths = new ArrayList<>();

    private List<Path> getPaths() {
        return paths;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public RegisteredDevice() {
    }
    public RegisteredDevice(String ipAddress) {
        this.ipAddress = ipAddress;
    }

}
