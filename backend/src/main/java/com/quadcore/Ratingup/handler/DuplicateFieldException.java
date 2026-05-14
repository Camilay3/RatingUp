package com.quadcore.Ratingup.handler;

import java.util.List;

public class DuplicateFieldException extends RuntimeException {
    private final List<String> erros;

    public DuplicateFieldException(List<String> erros) {
        super("Campos duplicados");
        this.erros = erros;
    }

    public List<String> getErros() { return erros; }
}