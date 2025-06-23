package com.example.bankcards.exception;

public class PhoneAlreadyExistsException extends RuntimeException {

    public PhoneAlreadyExistsException(String phone) {
        super(String.format("Phone %s already exists", phone));
    }
}
