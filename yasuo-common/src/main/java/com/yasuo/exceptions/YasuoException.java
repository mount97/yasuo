package com.yasuo.exceptions;

import lombok.Getter;

@Getter
public class YasuoException extends RuntimeException {
    private final String message;

    public YasuoException(String message) {
        super(message);
        this.message = message;
    }
}
