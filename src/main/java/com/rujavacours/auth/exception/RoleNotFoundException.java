package com.rujavacours.auth.exception;


import org.springframework.security.core.AuthenticationException;

public class RoleNotFoundException extends AuthenticationException {
    public RoleNotFoundException(String msg) {
        super(msg);
    }

    public RoleNotFoundException(String msg, Throwable t) {
        super(msg, t);
    }

}
