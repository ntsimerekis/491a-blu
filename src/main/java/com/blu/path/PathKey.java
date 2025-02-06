package com.blu.path;


import jakarta.persistence.*;

import java.io.Serializable;
import com.blu.user.User;

@Embeddable
public class PathKey implements Serializable {
/*
Used to create a composite key for Path.
 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="email",referencedColumnName = "email")
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
