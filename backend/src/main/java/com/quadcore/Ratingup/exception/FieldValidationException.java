package com.quadcore.Ratingup.exception;

public class FieldValidationException extends DomainException {
    private final String field;

    public FieldValidationException(String field, String message) {
        super(message, "VALIDATION_ERROR");
        this.field = field;
    }

    public String getField() {
        return field;
    }
}
