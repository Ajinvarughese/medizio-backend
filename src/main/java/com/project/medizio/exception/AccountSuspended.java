package com.project.medizio.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.LOCKED) // 423
public class AccountSuspended extends RuntimeException {
    public AccountSuspended(String message) {
        super(message);
    }
}
