package com.blu.path;

import com.blu.device.Device;
import com.blu.user.User;
import jakarta.persistence.*;

import java.util.Objects;

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
    private Device device;

    public Path() {

    }

    public Path(String name, String file, User user, Device device) {
        this.file = file;
        this.device = device;
        this.id = new PathKey(user,name);
    }

    public PathKey getId() {
        return id;
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

        return device.getIpAddress();
    }

    public String getUsername() {
        return id.getUser().getUsername();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Path)) return false;
        Path p = (Path) o;
        return Objects.equals(getId(), p.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

}
