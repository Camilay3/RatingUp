package com.quadcore.Ratingup.exception;

import java.util.List;
import java.util.Collections;

public class ConflictException extends DomainException {
    private final List<String> conflictDetails;

    public ConflictException(String message) {
        super(message, "CONFLICT");
        this.conflictDetails = Collections.emptyList();
    }

    public ConflictException(String message, List<String> conflictDetails) {
        super(message, "CONFLICT");
        this.conflictDetails = conflictDetails;
    }

    public List<String> getConflictDetails() {
        return conflictDetails;
    }
}
