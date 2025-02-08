package com.blu.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("confirmationTokenRepository")
public interface ConfirmationTokenRespository extends JpaRepository<ConfirmationToken, Long> {
    Optional<ConfirmationToken> findByConfirmationToken(String confirmationToken);
    boolean existsByConfirmationToken(String confirmationToken);
}
