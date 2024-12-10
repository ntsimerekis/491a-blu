package com.blu.model;


import jakarta.persistence.*;

import java.io.Serializable;
import com.blu.model.User;

@Embeddable
public class PathKey implements Serializable {
/*
Used to create a composite key for Path.
 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="username",referencedColumnName = "username")
    private User user;

    @Column(name="name",
            length=255)
    private String name;

    public PathKey setName(String name) {
        this.name = name;
        return this;
    }
    public String getName() {
        return name;
    }

    public User getUser() {
        return user;
    }

    public PathKey() {}
    public PathKey(User user, String name) {
        this.user = user;
        this.name = name;
    }
}
