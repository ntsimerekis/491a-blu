package com.blu.path;

import com.blu.device.RegisteredDevice;
import com.blu.user.User;
import jakarta.persistence.*;

@Entity
@Table(name = "paths")
public class Path {

    //Composite primary key, includes user(username) and name.
    @EmbeddedId
    private PathKey id;

    @Column(nullable = false,
            length = 255,
            unique=true)
    private String file;

    //foreign key
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="mac_address", referencedColumnName = "mac_address")
    private RegisteredDevice registeredDevice;


    public String getName() {
        return id.getName();
    }

    public void setName(String name) {
        this.id.setName(name);
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }


    public String getMacAddress() {

        return registeredDevice.getMacAddress();
    }

    public String getUsername() {
        return id.getUser().getUsername();
    }


    public Path(){}

   public Path(String name, String file, int deviceMac, User user, RegisteredDevice registeredDevice) {

        this.file = file;
        this.registeredDevice = registeredDevice;
        this.id = new PathKey(user,name);
    }

}
