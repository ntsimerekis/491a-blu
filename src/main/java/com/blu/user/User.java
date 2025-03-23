package com.blu.user;

import com.blu.device.Device;
import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/*
    Defines a user object/entity that extends the spring boot UserDetails class.
 */
@Table(name = "users")
@Entity
public class User implements UserDetails {

    @Value("${blu.auth.email-verification}")
    private static boolean requireEmailVerification;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer id;

    @Column(nullable = false)
    private String fullName;

    @Column(unique = true, length = 100, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(nullable = false)
    @ColumnDefault("false")
    private boolean admin;

    @Column(nullable = false)
    @ColumnDefault("false")
    private boolean emailVerified;

    @OneToMany
    private List<Device> devices;

    @OneToOne
    private Device activeDevice;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }


    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonLocked() {
        if (requireEmailVerification) {
            return this.emailVerified;
        }
        return true;
    }

    public String getFullName() {
        return fullName;
    }

    public User setFullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public User verifyUser(){
        this.emailVerified = true;
        return this;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {this.emailVerified = emailVerified;}

    public void addDevice(Device device) {
        this.devices.add(device);
    }

    public List<Device> getDevices() {
        return devices;
    }

    public Device getActiveDevice() {
        return activeDevice;
    }

    public User(){}

    public User(String email, String password, boolean admin) {
        this.email = email;
        this.password = password;
        this.admin = admin;
        this.devices = new ArrayList<>();
    }
}