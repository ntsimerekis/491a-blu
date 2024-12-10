package com.blu.model;

import jakarta.persistence.*;

import java.util.List;
import java.util.ArrayList;

import com.blu.model.Path;

@Entity
@Table(name = "user")
public class User {
    @Id
    @Column(nullable = false,
            length = 25)
    private String username;

    @Column(nullable = false,
            length = 64)
    private String password;

    @Column(nullable = false)
    private int admin;

    //getters and setters

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public int getAdmin() {
        return admin;
    }
    public void setAdmin(int admin) {
        this.admin = admin;
    }

    //This might be necessary,
    // but i cant get it to work with PathKey,
    //and i havent seen any issues yet.
    /*
    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Path> paths = new ArrayList<>();
    */

    public User(){}

    public User(String username, String password, int admin) {
        this.username = username;
        this.password = password;
        this.admin = admin;
    }
}
