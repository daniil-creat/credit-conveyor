package com.example.conveyor.exceptions;

public class AgeException extends RuntimeException {
    public AgeException() {
    }
    public AgeException(String message) {
        super(message);
    }
}
