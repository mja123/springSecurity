package com.mja123.security.controller.exceptions;

public enum ErrorTypes {
    NOT_UNIQUE("not-unique-error"),
    NOT_FOUND("not-found-error"),
    ARGUMENT_VALIDATION("argument-validation-error"),
    ARGUMENT_MISMATCH("argument-type-mismatch-error"),
    UNKNOWN_ERROR("unknown-error");

    final String error;
    ErrorTypes(String error) {
        this.error = error;
    }
}
