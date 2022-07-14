package com.example.conveyor.exceptions;

public class CreditException extends RuntimeException {
        public CreditException() {
        }
        public CreditException(String message) {
            super(message);
        }
}
