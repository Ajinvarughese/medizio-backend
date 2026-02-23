package com.project.medizio.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)  // 409
public class DoctorUnavailableException extends RuntimeException {

    public DoctorUnavailableException(String message) {
        super(message);
    }
}