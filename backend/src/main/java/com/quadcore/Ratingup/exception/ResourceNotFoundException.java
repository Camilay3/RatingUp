package com.quadcore.Ratingup.exception;

public class ResourceNotFoundException extends DomainException {
    public ResourceNotFoundException(String message) {
        super(message, "NOT_FOUND");
    }

    public ResourceNotFoundException(String message, String errorCode) {
        super(message, errorCode);
    }
}
