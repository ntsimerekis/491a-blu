package com.blu.auth.exception;

import org.springframework.security.core.AuthenticationException;

/*
    To be thrown when there is an error with email verification
 */
public class EmailVerificationException extends AuthenticationException {
    public EmailVerificationException(String msg) {
        super(msg);
    }
}
