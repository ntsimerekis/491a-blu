package com.blu.device;

import com.blu.path.Path;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="registered_devices")
public class RegisteredDevice {

    @Id
    @Column(name="mac_address",
            length=24,
            nullable=false)
    private String macAddress;

    @Column(name="ip_address",
            nullable=false)
    private int ipAddress;

    @Column(nullable=false)
    private boolean active;

    private int size;

    @OneToMany(
            mappedBy = "registeredDevice",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Path> paths = new ArrayList<>();

    public String getMacAddress() {
        return macAddress;
    }

    public int getIpAddress() {
        return ipAddress;
    }

    public RegisteredDevice() {
    }
    public RegisteredDevice(String macAddress, int ipAddress) {
        this.macAddress = macAddress;
        this.ipAddress = ipAddress;
    }

}
