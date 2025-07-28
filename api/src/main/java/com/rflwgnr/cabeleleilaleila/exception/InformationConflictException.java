package com.rflwgnr.cabeleleilaleila.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class InformationConflictException extends RuntimeException {
    public InformationConflictException(String message) {
        super(message);
    }
}
