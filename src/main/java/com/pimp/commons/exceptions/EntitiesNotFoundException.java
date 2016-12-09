package com.pimp.commons.exceptions;

public class EntitiesNotFoundException extends RuntimeException {

    public EntitiesNotFoundException() {
    }

    public EntitiesNotFoundException(String message) {
        super(message);
    }
}
