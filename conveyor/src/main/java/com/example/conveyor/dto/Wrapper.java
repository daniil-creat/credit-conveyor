package com.example.conveyor.dto;

import lombok.Getter;

@Getter
public class Wrapper<T> {
    private final boolean success;
    private final T result;
    private final String error;

    public Wrapper(T result) {
        this.success = true;
        this.result = result;
        this.error = null;
    }

    public Wrapper(Throwable cause) {
        this.success = false;
        this.result = null;
        this.error = cause.getMessage();
    }
}
