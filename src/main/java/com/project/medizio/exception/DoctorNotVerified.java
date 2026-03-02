package com.project.medizio.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN) // 403
public class DoctorNotVerified extends RuntimeException {
    public DoctorNotVerified(String message) {
        super(message);
    }
}
