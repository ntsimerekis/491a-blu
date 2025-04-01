package com.blu.device;

import com.blu.user.User;
import jakarta.persistence.*;

@Entity
@Table(name="devices")
public class Device {

//    @Id
//    @Column(name="mac_address",
//            length=24,
//            nullable=false)
//    private String macAddress;

    @Id
    @Column(name="ip_address",
            nullable=false)
    private String ipAddress;

    @Column(name="name",
            nullable=false)
    private String name;

    private int size;

    @Column(name="active", nullable=false)
    private boolean active;

//    @OneToMany(
//            mappedBy = "registeredDevice",
//            cascade = CascadeType.ALL,
//            orphanRemoval = true
//    )
//
//    private List<Path> getPaths() {
//        return paths;
//    }

    @ManyToOne
    private User owner;

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Device() {
    }

    public Device(String name, String ipAddress, User owner, boolean active) {
        this.ipAddress = ipAddress;
        this.owner = owner;
        this.name = name;
        //Need to change later
        this.size = 0;

        //
        this.active = active;
    }

}
