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
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private RegisteredDevice registeredDevice;

    public Path() {

    }

    public Path(String name, String file, User user, RegisteredDevice registeredDevice) {
        this.file = file;
        this.registeredDevice = registeredDevice;
        this.id = new PathKey(user,name);
    }

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

    public String getIpAddress() {

        return registeredDevice.getIpAddress();
    }

    public String getUsername() {
        return id.getUser().getUsername();
    }

}
