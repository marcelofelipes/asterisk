package com.asterisk.backend.infrastructure.exception;

public class UserNotFoundException extends Exception{

    public UserNotFoundException(final String message) {
        super(message);
    }

    public UserNotFoundException() {
    }
}
