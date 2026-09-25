package com.quadcore.Ratingup.exception;

public class UnauthorizedOperationException extends DomainException {
    public UnauthorizedOperationException(String message) {
        super(message, "UNAUTHORIZED");
    }

    public UnauthorizedOperationException(String message, String errorCode) {
        super(message, errorCode);
    }
}
