package com.rujavacours.auth.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class JsonException {
    private String exception;
    private String message;
}
