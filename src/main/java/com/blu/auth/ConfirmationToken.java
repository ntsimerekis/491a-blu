package com.blu.auth;

import com.blu.user.User;
import jakarta.persistence.*;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;
import java.util.Random;

@Entity
public class ConfirmationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "token_id")
    private Long tokenId;

    @Column(name = "confirmation_token")
    private String confirmationToken;

    @OneToOne
    @JoinColumn(name = "id", nullable = false)
    private User user;

    public ConfirmationToken() {

    }

    public ConfirmationToken(User user) {
        this.user = user;
        this.confirmationToken = generateRandomToken();
    }

    private String generateRandomToken() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[32]; // Generates a 32-byte token
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private Long generateRandomId() {
        return new Random().nextLong(100000, 999999);
    }

    public Long getTokenId() {
        return tokenId;
    }

    public void setTokenId(Long tokenId) {
        this.tokenId = tokenId;
    }

    public String getConfirmationToken() {
        return confirmationToken;
    }

    public void setConfirmationToken(String confirmationToken) {
        this.confirmationToken = confirmationToken;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
