package com.blu.path;


import jakarta.persistence.*;

import java.io.Serializable;
import com.blu.user.User;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/*
    Used to create a composite key for Path.
 */
@Embeddable
public class PathKey implements Serializable {

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
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
