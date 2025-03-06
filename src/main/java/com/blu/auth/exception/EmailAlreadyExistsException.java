package com.blu.auth.exception;

// Luke T.
// Response message when a user inputs an email that already exists
public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException(String message) {
        super(message);
    }
}
