package com.blu.user;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Table(name = "users")
@Entity
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.emailVerified;
    }

    @Override
    public boolean isCredentialsNonExpired() {
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

    public User setEmailVerified(boolean emailVerified) {this.emailVerified = emailVerified;return this;}

    public User(){}

    public User(String email, String password, boolean admin) {
        this.email = email;
        this.password = password;
        this.admin = admin;
    }
}